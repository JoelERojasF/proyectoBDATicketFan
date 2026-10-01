/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.AdministradorEntidad;
import dto.EditarAdministradorDTO;
import dto.GuardarAdministradorDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author le0jx
 */
public class AdministradorDAO implements IAdministradorDAO{
    
    private IConexionBD conexion;

    public AdministradorDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    public boolean validarUsuarioDisponible(String usuario) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
                String sentenciaSQL = """
                                  SELECT 1 FROM administrador WHERE usuario = ? LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, usuario);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar usuario de administrador: " + e.getMessage());
        }
    }

    /** Igual que validarUsuarioDisponible, pero ignora al propio administrador (para editar). */
    public boolean validarUsuarioDisponible(String usuario, int idExcluir) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 1 FROM administrador WHERE usuario = ? AND id_administrador <> ? LIMIT 1
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setString(1, usuario);
            comando.setInt(2, idExcluir);
            try (ResultSet rs = comando.executeQuery()) {
                return !rs.next();
            }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar usuario de administrador: " + e.getMessage());
        }
    }

    @Override
    public AdministradorEntidad guardarAdministrador(GuardarAdministradorDTO registro) throws PersistenciaException {
        if(!validarUsuarioDisponible(registro.getUsuario())) throw new PersistenciaException("Error usuario de administrador ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO administrador (nombres,
                                                      apellido_paterno,
                                                      apellido_materno,
                                                      usuario,
                                                      contrasena) 
                                  VALUES (?,?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
            
            String contrasenia = BCrypt.hashpw(registro.getContraseña(), BCrypt.gensalt());

            comando.setString(1, registro.getNombres());
            comando.setString(2, registro.getApellidoPaterno());
            comando.setString(3, registro.getApellidoMaterno());
            comando.setString(4, registro.getUsuario());
            comando.setString(5, contrasenia);

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar administrador: " + e.getMessage());
        }
    }

    @Override
    public AdministradorEntidad editarAdministrador(EditarAdministradorDTO registro) throws PersistenciaException {
        if(!validarUsuarioDisponible(registro.getUsuario(), registro.getId())) throw new PersistenciaException("Error usuario de administrador ya registrado");
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE administrador 
                                  SET nombres = ?,
                                      apellido_paterno = ?,
                                      apellido_materno = ?,
                                      usuario = ?, 
                                      contrasena = ?,
                                      id_promotora = ?
                                  WHERE id_administrador = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            
            String contrasenia = BCrypt.hashpw(registro.getContraseña(), BCrypt.gensalt());

            comando.setString(1, registro.getNombres());
            comando.setString(2, registro.getApellidoPaterno());
            comando.setString(3, registro.getApellidoMaterno());
            comando.setString(4, registro.getUsuario());
            comando.setString(5, contrasenia);
            comando.setInt(6, registro.getIdPromotora());
            comando.setInt(7, registro.getId());

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar administrador: " + e.getMessage());
        }
    }

    @Override
    public AdministradorEntidad eliminarAdministrador(int id) throws PersistenciaException {
        AdministradorEntidad administrador = BuscarPorID(id);
        if (administrador == null) {
            throw new PersistenciaException("No existe el administrador con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM administrador WHERE id_administrador = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return administrador;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar administrador: " + e.getMessage());
        }    
    }

    @Override
    public AdministradorEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_administrador,
                                    nombres,
                                    apellido_paterno,
                                    apellido_materno,
                                    usuario,
                                    contrasena,
                                    id_promotora
                                  FROM administrador 
                                  WHERE id_administrador = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            if (rs.next()) {
                return new AdministradorEntidad(
                        rs.getInt("id_administrador"),
                        rs.getString("nombres"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getObject("id_promotora", Integer.class));
            }

            throw new PersistenciaException("No existe el administrador con id: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar administrador: " + e.getMessage());
        }    
    }

    @Override
    public List<AdministradorEntidad> listarAdministradores(String filtro) throws PersistenciaException {
        List<AdministradorEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_administrador,
                                    nombres,
                                    apellido_paterno,
                                    apellido_materno,
                                    usuario,
                                    contrasena,
                                    id_promotora
                                    FROM administrador 
                                  WHERE nombres LIKE ? OR apellido_paterno LIKE ? OR apellido_materno LIKE ? OR usuario LIKE ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);
            comando.setString(3, comodinBusqueda);
            comando.setString(4, comodinBusqueda);

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                lista.add(new AdministradorEntidad(
                        rs.getInt("id_administrador"),
                        rs.getString("nombres"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getObject("id_promotora", Integer.class)
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar administradores: " + e.getMessage());
        }
    }
    
}
