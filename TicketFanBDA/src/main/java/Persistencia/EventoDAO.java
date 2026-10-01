/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.EventoEntidad;
import dto.EditarEventoDTO;
import dto.GuardarEventoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class EventoDAO implements IEventoDAO{
    
    private IConexionBD conexion;

    public EventoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    private boolean validarNombreDisponible(String nombre_show) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
                String sentenciaSQL = """
                                  SELECT 1 FROM evento WHERE nombre_show = ? LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, nombre_show);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar nombre del evento: " + e.getMessage());
        }
    }

    @Override
    public EventoEntidad guardarEvento(GuardarEventoDTO registro) throws PersistenciaException {
        if(!validarNombreDisponible(registro.getNombreShow())) throw new PersistenciaException("Error nombre del evento ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO evento (nombre_show,
                                                      cantidad_boletos,
                                                      id_administrador) 
                                  VALUES (?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            comando.setString(1, registro.getNombreShow());
            comando.setInt(2, registro.getCantidadBoletos());
            comando.setInt(3, registro.getIdAdministrador());

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar evento: " + e.getMessage());
        }
    }

    @Override
    public EventoEntidad editarEvento(EditarEventoDTO registro) throws PersistenciaException {
        if(!validarNombreDisponible(registro.getNombreShow())) throw new PersistenciaException("Error nombre del evento ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE evento 
                                  SET nombre_show = ?,
                                      tipo = ?,
                                      edad_minima = ?,
                                      imagen_promocional = ?,
                                      cantidad_boletos = ?,
                                      id_administrador = ?,
                                      id_cuenta = ?,
                                  WHERE id_evento = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            comando.setString(1, registro.getNombreShow());
            comando.setString(2, registro.getTipo());
            comando.setInt(3, registro.getEdadMinima());
            comando.setString(4, registro.getImagenPromocional());
            comando.setInt(5, registro.getCantidadBoletos());
            comando.setInt(6, registro.getIdAdministrador());
            comando.setInt(7, registro.getIdCuenta());
            comando.setInt(8, registro.getId());
            

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar evento: " + e.getMessage());
        }
    }

    @Override
    public EventoEntidad eliminarEvento(int id) throws PersistenciaException {
        EventoEntidad evento = BuscarPorID(id);
        if (evento == null) {
            throw new PersistenciaException("No existe el evento con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM evento WHERE id_evento = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return evento;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar evento: " + e.getMessage());
        }
    }

    @Override
    public EventoEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_evento,
                                    nombre_show,
                                    tipo,
                                    edad_minima,
                                    imagen_promocional,
                                    cantidad_boletos,
                                    id_administrador,
                                    id_cuenta
                                  FROM evento 
                                  WHERE id_evento = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            if (rs.next()) {
                return new EventoEntidad(
                        rs.getInt("id_cuenta"),
                        rs.getString("nombre_show"),
                        rs.getString("tipo"),
                        rs.getInt("edad_minima"),
                        rs.getString("imagen_promocional"),
                        rs.getInt("cantidad_boletos"),
                        rs.getInt("id_administrador"),
                        rs.getInt("id_cuenta"));
            }

            throw new PersistenciaException("No existe el evento con id: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar evento: " + e.getMessage());
        }
    }

    @Override
    public List<EventoEntidad> listarEventos(String filtro) throws PersistenciaException {
        List<EventoEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_evento,
                                    nombre_show,
                                    tipo,
                                    edad_minima,
                                    imagen_promocional,
                                    cantidad_boletos,
                                    id_administrador,
                                    id_cuenta
                                  FROM evento 
                                  WHERE nombre_show LIKE ? OR tipo LIKE ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                lista.add(new EventoEntidad(
                        rs.getInt("id_evento"),
                        rs.getString("nombre_show"),
                        rs.getString("tipo"),
                        rs.getInt("edad_minima"),
                        rs.getString("imagen_promocional"),
                        rs.getInt("cantidad_boletos"),
                        rs.getInt("id_administrador"),
                        rs.getInt("id_cuenta")
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar eventos: " + e.getMessage());
        }
    }
    
}
