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
