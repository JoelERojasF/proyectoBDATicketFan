/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.CompraEntidad;
import dto.EditarCompraDTO;
import dto.GuardarCompraDTO;
import dto.BoletoPDFDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class CompraDAO implements ICompraDAO{
    
    private IConexionBD conexion;

    public CompraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
public BoletoPDFDTO obtenerDatosBoletoPDF(int idCompra) throws PersistenciaException {

    String comando = """
        SELECT
            c.fecha_hora,
            c.total,

            cl.nombres,
            cl.apellido_paterno,
            cl.apellido_materno,

            b.numero_boleto,
            b.codigo_boleto,
            b.precio,

            e.nombre_show,
            e.tipo,
            e.edad_minima

        FROM compra c

        INNER JOIN cliente cl
            ON c.id_cliente = cl.id_cliente

        INNER JOIN boleto b
            ON b.id_compra = c.id_compra

        INNER JOIN evento e
            ON b.id_evento = e.id_evento

        WHERE c.id_compra = ?
        """;

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement comandoSQL = conexion.prepareStatement(comando)) {

        comandoSQL.setInt(1, idCompra);

        try (ResultSet resultado = comandoSQL.executeQuery()) {

            if (resultado.next()) {

                BoletoPDFDTO boleto = new BoletoPDFDTO();

                boleto.setNombreCliente(
                    resultado.getString("nombres") + " "
                    + resultado.getString("apellido_paterno") + " "
                    + resultado.getString("apellido_materno")
                );

                boleto.setNombreEvento(
                    resultado.getString("nombre_show")
                );

                boleto.setTipoEvento(
                    resultado.getString("tipo")
                );

                boleto.setEdadMinima(
                    resultado.getInt("edad_minima")
                );

                boleto.setNumeroBoleto(
                    resultado.getString("numero_boleto")
                );

                boleto.setCodigoBoleto(
                    resultado.getString("codigo_boleto")
                );

                boleto.setPrecio(
                    resultado.getDouble("precio")
                );

                boleto.setFechaCompra(
                    resultado.getTimestamp("fecha_hora")
                        .toLocalDateTime()
                );

                return boleto;
            }

            return null;

        }

    } catch (SQLException e) {
        throw new PersistenciaException(
            "Error al obtener los datos del boleto", e
        );
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

            return null;
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
