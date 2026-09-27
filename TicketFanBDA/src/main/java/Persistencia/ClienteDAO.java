/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.ClienteEntidad;
import dto.EditarClienteDTO;
import dto.GuardarClienteDTO;
import java.sql.Connection;
import java.sql.Date;
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
public class ClienteDAO implements IClienteDAO{
    
    private IConexionBD conexion;

    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public ClienteEntidad guardarCliente(GuardarClienteDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO cliente (nombres,
                                                      apellido_paterno,
                                                      apellido_materno,
                                                      fecha_nacimiento,
                                                      usuario,
                                                      contrasena) 
                                  VALUES (?,?,?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            String contrasenia = BCrypt.hashpw(registro.getContraseña(), BCrypt.gensalt());
            Date fecha = Date.valueOf(registro.getFechaNacimiento());

            comando.setString(1, registro.getNombres());
            comando.setString(2, registro.getApellidoPaterno());
            comando.setString(3, registro.getApellidoMaterno());
            comando.setDate(4, fecha);
            comando.setString(5, registro.getUsuario());
            comando.setString(6, contrasenia);

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar cliente: " + e.getMessage());
        }
    }

    @Override
    public ClienteEntidad editarCliente(EditarClienteDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE cliente 
                                  SET nombres = ?,
                                      apellido_paterno = ?,
                                      apellido_materno = ?,
                                      fecha_nacimiento = ?,
                                      usuario = ?, 
                                      contrasena = ?,
                                  WHERE id_cliente = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String contrasenia = BCrypt.hashpw(registro.getContraseña(), BCrypt.gensalt());
            Date fecha = Date.valueOf(registro.getFechaNacimiento());

            comando.setString(1, registro.getNombres());
            comando.setString(2, registro.getApellidoPaterno());
            comando.setString(3, registro.getApellidoMaterno());
            comando.setDate(4, fecha);
            comando.setString(5, registro.getUsuario());
            comando.setString(6, contrasenia);
            comando.setInt(7, registro.getId());

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar cliente: " + e.getMessage());
        }
    }

    @Override
    public ClienteEntidad eliminarCliente(int id) throws PersistenciaException {
        ClienteEntidad cliente = BuscarPorID(id);
        if (cliente == null) {
            throw new PersistenciaException("No existe el cliente con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM cliente WHERE cliente = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return cliente;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar cliente: " + e.getMessage());
        }
    }

    @Override
    public ClienteEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_cliente,
                                    nombres,
                                    apellido_paterno,
                                    apellido_materno,
                                    fecha_nacimiento,
                                    usuario,
                                    contrasena
                                  FROM cliente 
                                  WHERE id_cliente = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            
            if (rs.next()) {
                return new ClienteEntidad(
                        rs.getInt("id_cliente"),
                        rs.getString("nombres"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario"),
                        rs.getString("contrasena"));
            }

            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar cliente: " + e.getMessage());
        }
    }

    @Override
    public List<ClienteEntidad> listarClientes(String filtro) throws PersistenciaException {
        List<ClienteEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_cliente,
                                    nombres,
                                    apellido_paterno,
                                    apellido_materno,
                                    fecha_nacimiento,
                                    usuario,
                                    contrasena
                                  FROM cliente 
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
                lista.add(new ClienteEntidad(
                       rs.getInt("id_cliente"),
                        rs.getString("nombres"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listas clientes: " + e.getMessage());
        }
    }
    
}
