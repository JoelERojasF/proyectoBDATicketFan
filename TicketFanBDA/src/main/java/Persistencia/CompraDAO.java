/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.CompraEntidad;
import dto.EditarCompraDTO;
import dto.GuardarCompraDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author le0jx
 */
public class CompraDAO implements ICompraDAO{
    
    private IConexionBD conexion;

    public CompraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Cancela una compra en UNA sola transaccion (todo o nada):
     *  1) marca la compra como cancelada,
     *  2) reembolsa el total a la cuenta del cliente,
     *  3) descuenta de la cuenta de cada promotora lo que cobro por los boletos de esta compra,
     *  4) libera los boletos (id_compra = NULL) para que puedan venderse de nuevo.
     */
    @Override
    public CompraEntidad cancelarCompraConReembolso(int idCompra) throws PersistenciaException {
        try (Connection con = this.conexion.crearConexion()) {
            con.setAutoCommit(false);
            try {
                double total;
                int idCuentaCliente;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT total, estatus, id_cuenta FROM compra WHERE id_compra = ? FOR UPDATE")) {
                    ps.setInt(1, idCompra);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new PersistenciaException("No existe la compra con id: " + idCompra);
                        }
                        if ("cancelado".equalsIgnoreCase(rs.getString("estatus"))) {
                            throw new PersistenciaException("La compra ya fue cancelada anteriormente.");
                        }
                        total = rs.getDouble("total");
                        idCuentaCliente = rs.getInt("id_cuenta");
                    }
                }

                // 1) estatus
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE compra SET estatus = 'cancelado' WHERE id_compra = ?")) {
                    ps.setInt(1, idCompra);
                    ps.executeUpdate();
                }

                // 2) reembolso al cliente
                modificarSaldo(con, idCuentaCliente, total);

                // 3) descuento a la cuenta de cada promotora (segun el evento de cada boleto)
                Map<Integer, Double> montosPorCuenta = new LinkedHashMap<>();
                try (PreparedStatement ps = con.prepareStatement("""
                        SELECT e.id_cuenta, SUM(b.precio) AS monto
                        FROM boleto b
                        INNER JOIN evento e ON b.id_evento = e.id_evento
                        WHERE b.id_compra = ? AND e.id_cuenta IS NOT NULL
                        GROUP BY e.id_cuenta
                        """)) {
                    ps.setInt(1, idCompra);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            montosPorCuenta.put(rs.getInt("id_cuenta"), rs.getDouble("monto"));
                        }
                    }
                }
                for (Map.Entry<Integer, Double> cuenta : montosPorCuenta.entrySet()) {
                    modificarSaldo(con, cuenta.getKey(), -cuenta.getValue());
                }

                // 4) liberar boletos
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE boleto SET id_compra = NULL WHERE id_compra = ?")) {
                    ps.setInt(1, idCompra);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException | PersistenciaException e) {
                con.rollback();
                if (e instanceof PersistenciaException) {
                    throw (PersistenciaException) e;
                }
                throw new PersistenciaException("Error al cancelar compra: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al cancelar compra: " + e.getMessage());
        }
        return BuscarPorID(idCompra);
    }

    /** Suma (o resta, si el monto es negativo) un monto al saldo, dentro de la transaccion recibida. */
    private void modificarSaldo(Connection con, int idCuenta, double monto) throws SQLException, PersistenciaException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE cuenta SET saldo = saldo + ? WHERE id_cuenta = ?")) {
            ps.setDouble(1, monto);
            ps.setInt(2, idCuenta);
            if (ps.executeUpdate() != 1) {
                throw new PersistenciaException("No existe la cuenta con id: " + idCuenta);
            }
        }
    }

    @Override
    public CompraEntidad guardarCompra(GuardarCompraDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO compra (detalles,
                                                      total,
                                                      fecha_hora,
                                                      id_cliente,
                                                      id_cuenta) 
                                  VALUES (?,?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            Timestamp fechaHora = Timestamp.valueOf(registro.getFechaHora());
            
            comando.setString(1, registro.getDetalles());
            comando.setDouble(2, registro.getTotal());
            comando.setTimestamp(3, fechaHora);
            comando.setInt(4, registro.getIdCliente());
            comando.setInt(5, registro.getIdCuenta());

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar compra: " + e.getMessage());
        }
    }

    @Override
    public CompraEntidad editarCompra(EditarCompraDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE compra 
                                  SET detalles = ?,
                                      total = ?,
                                      estatus = ?,
                                      fecha_hora = ?,
                                      id_cliente = ?,
                                      id_cuenta = ?
                                  WHERE id_compra = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            
            Timestamp fechaHora = Timestamp.valueOf(registro.getFechaHora());

            comando.setString(1, registro.getDetalles());
            comando.setDouble(2, registro.getTotal());
            comando.setString(3, registro.getEstatus());
            comando.setTimestamp(4,fechaHora);
            comando.setInt(5, registro.getIdCliente());
            comando.setInt(6, registro.getIdCuenta());
            comando.setInt(7, registro.getId());

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar compra: " + e.getMessage());
        }
    }

    @Override
    public CompraEntidad eliminarCompra(int id) throws PersistenciaException {
        CompraEntidad compra = BuscarPorID(id);
        if (compra == null) {
            throw new PersistenciaException("No existe la compra con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM compra WHERE id_compra = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return compra;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar compra: " + e.getMessage());
        }
    }

    @Override
    public CompraEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_compra,
                                    detalles,
                                    total,
                                    estatus,
                                    fecha_hora,
                                    id_cliente,
                                    id_cuenta
                                  FROM compra 
                                  WHERE id_compra = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            
            if (rs.next()) {
                return new CompraEntidad(
                        rs.getInt("id_compra"),
                        rs.getString("detalles"),
                        rs.getDouble("total"),
                        rs.getString("estatus"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_cuenta"));
            }

            throw new PersistenciaException("No existe la compra con id: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar compra: " + e.getMessage());
        }
    }

    @Override
    public List<CompraEntidad> listarCompras(String filtro) throws PersistenciaException {
        List<CompraEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_compra,
                                    detalles,
                                    total,
                                    estatus,
                                    fecha_hora,
                                    id_cliente,
                                    id_cuenta
                                  FROM compra 
                                  WHERE detalles LIKE ? OR estatus LIKE ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                lista.add(new CompraEntidad(
                        rs.getInt("id_compra"),
                        rs.getString("detalles"),
                        rs.getDouble("total"),
                        rs.getString("estatus"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_cuenta")
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar compras: " + e.getMessage());
        }
    }
    
}
