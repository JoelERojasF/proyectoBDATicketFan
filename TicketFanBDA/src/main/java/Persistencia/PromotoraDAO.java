/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.PromotoraEntidad;
import dto.EditarPromotoraDTO;
import dto.GuardarPromotoraDTO;
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
public class PromotoraDAO implements IPromotoraDAO{
    
    private final IConexionBD conexion;

    public PromotoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    private boolean validarNombreDisponible(String nombre) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
                String sentenciaSQL = """
                                  SELECT 1 FROM promotora WHERE nombre = ? LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, nombre);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar nombre de promotora: " + e.getMessage());
        }
    }
    
    private boolean validarDireccionDisponible(String colonia, String calle, String numero, String ciudad, String estado) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 1 FROM promotora 
                                  WHERE colonia = ? AND calle = ? AND numero = ? AND ciudad = ? AND estado = ? 
                                  LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, colonia);
                comando.setString(2, calle);
                comando.setString(3, numero);
                comando.setString(4, ciudad);
                comando.setString(5, estado);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar dirección de promotora: " + e.getMessage());
        }
    }

    /** Igual que validarNombreDisponible, pero ignora a la propia promotora (para editar). */
    private boolean validarNombreDisponible(String nombre, int idExcluir) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 1 FROM promotora WHERE nombre = ? AND id_promotora <> ? LIMIT 1
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setString(1, nombre);
            comando.setInt(2, idExcluir);
            try (ResultSet rs = comando.executeQuery()) {
                return !rs.next();
            }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar nombre de promotora: " + e.getMessage());
        }
    }

    /** Igual que validarDireccionDisponible, pero ignora a la propia promotora (para editar). */
    private boolean validarDireccionDisponible(String colonia, String calle, String numero, String ciudad, String estado, int idExcluir) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 1 FROM promotora 
                                  WHERE colonia = ? AND calle = ? AND numero = ? AND ciudad = ? AND estado = ? 
                                    AND id_promotora <> ?
                                  LIMIT 1
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setString(1, colonia);
            comando.setString(2, calle);
            comando.setString(3, numero);
            comando.setString(4, ciudad);
            comando.setString(5, estado);
            comando.setInt(6, idExcluir);
            try (ResultSet rs = comando.executeQuery()) {
                return !rs.next();
            }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar dirección de promotora: " + e.getMessage());
        }
    }

    @Override
    public PromotoraEntidad guardarPromotora(GuardarPromotoraDTO registro) throws PersistenciaException {
        if(!validarNombreDisponible(registro.getNombre())) throw new PersistenciaException("Error nombre de la promotora ya registrado");
        if(!validarDireccionDisponible(registro.getColonia(), registro.getCalle(), registro.getNumero(), registro.getCiudad(), registro.getEstado())) throw new PersistenciaException("Error direccion de la promotora ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
             String sentenciaSQL = """
                                  INSERT INTO promotora (nombre,
                                                      colonia,
                                                      calle,
                                                      numero,
                                                      ciudad,
                                                      estado) 
                                  VALUES (?,?,?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
            
            comando.setString(1, registro.getNombre());
            comando.setString(2, registro.getColonia());
            comando.setString(3, registro.getCalle());
            comando.setString(4, registro.getNumero());
            comando.setString(5, registro.getCiudad());
            comando.setString(6, registro.getEstado());
            
            comando.executeUpdate();
            
            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;
            
        }catch (SQLException e) {
            throw new PersistenciaException("Error al guardar promotora: " + e.getMessage());
        }
    }

    @Override
    public PromotoraEntidad editarPromotora(EditarPromotoraDTO registro) throws PersistenciaException {
        if(!validarNombreDisponible(registro.getNombre(), registro.getId())) throw new PersistenciaException("Error nombre de la promotora ya registrado");
        if(!validarDireccionDisponible(registro.getColonia(), registro.getCalle(), registro.getNumero(), registro.getCiudad(), registro.getEstado(), registro.getId())) throw new PersistenciaException("Error direccion de la promotora ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE promotora 
                                  SET nombre = ?,
                                      colonia = ?,
                                      calle = ?,
                                      numero = ?, 
                                      ciudad = ?,
                                      estado = ?
                                  WHERE id_promotora = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setString(1, registro.getNombre());
            comando.setString(2, registro.getColonia());
            comando.setString(3, registro.getCalle());
            comando.setString(4, registro.getNumero());
            comando.setString(5, registro.getCiudad());
            comando.setString(6, registro.getEstado());
            comando.setInt(7, registro.getId());
            
            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar promotora: " + e.getMessage());
        }    
    }

    @Override
    public PromotoraEntidad eliminarPromotora(int id) throws PersistenciaException {
        PromotoraEntidad promotora = BuscarPorID(id);
        if(promotora == null){
            throw new PersistenciaException("No existe la promotora con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM promotora WHERE id_promotora = ?";
            
            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();
            
            return promotora;
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar promotora: " + e.getMessage());
        }

    }

    @Override
    public PromotoraEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_promotora,
                                    nombre,
                                    colonia,
                                    calle,
                                    numero,
                                    ciudad,
                                    estado
                                  FROM promotora 
                                  WHERE id_promotora = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);
            
            ResultSet rs = comando.executeQuery();
            if (rs.next()) {
                return new PromotoraEntidad(
                        rs.getInt("id_promotora"),
                        rs.getString("nombre"), 
                        rs.getString("colonia"), 
                        rs.getString("calle"), 
                        rs.getString("numero"), 
                        rs.getString("ciudad"), 
                        rs.getString("estado"));
            }
            
            throw new PersistenciaException("No existe la promotora con id: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar promotora: " + e.getMessage());
        }

    }

    @Override
    public List<PromotoraEntidad> listarPromotoras(String filtro) throws PersistenciaException {
        List<PromotoraEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_promotora,
                                    nombre,
                                    colonia,
                                    calle,
                                    numero,
                                    ciudad,
                                    estado
                                  FROM promotora 
                                  WHERE nombre LIKE ? OR colonia LIKE ? OR calle LIKE ? OR numero LIKE ? OR ciudad LIKE ? OR estado LIKE ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);
            comando.setString(3, comodinBusqueda);
            comando.setString(4, comodinBusqueda);
            comando.setString(5, comodinBusqueda);
            comando.setString(6, comodinBusqueda);
            
            ResultSet rs = comando.executeQuery();
            
            while (rs.next()) {
                lista.add(new PromotoraEntidad(
                    rs.getInt("id_promotora"),
                    rs.getString("nombre"),
                    rs.getString("colonia"),
                    rs.getString("calle"),
                    rs.getString("numero"),
                    rs.getString("ciudad"), 
                    rs.getString("estado")
                ));
            }
            return lista;
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar promotoras: " + e.getMessage());
        }

    }
    
}
