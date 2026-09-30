/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.BoletoEntidad;
import dto.EditarBoletoDTO;
import dto.GuardarBoletoDTO;
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
public class BoletoDAO implements IBoletoDAO{
    
    private IConexionBD conexion;

    public BoletoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    public boolean boletoYaComprado(int idBoleto) throws PersistenciaException {

    String comando = """
        SELECT id_compra
        FROM boleto
        WHERE id_boleto = ?
          AND id_compra IS NOT NULL
        """;

    ConexionBD conexionBD = new ConexionBD();

    try (Connection conexion = conexionBD.crearConexion();
         PreparedStatement comandoSQL = conexion.prepareStatement(comando)) {

        comandoSQL.setInt(1, idBoleto);

        try (ResultSet resultado = comandoSQL.executeQuery()) {
            return resultado.next();
        }

    } catch (SQLException e) {
        throw new PersistenciaException(
            "Error al verificar si el boleto ya fue comprado"
        );
    }
}

    @Override
    public BoletoEntidad guardarBoleto(GuardarBoletoDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO boleto (numero_boleto,
                                                      codigo_boleto,
                                                      precio,
                                                      id_evento) 
                                  VALUES (?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            comando.setString(1, registro.getNumBoleto());
            comando.setString(2, registro.getCodigoBoleto());
            comando.setDouble(3, registro.getPrecio());
            comando.setInt(4, registro.getIdEvento());

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar boleto: " + e.getMessage());
        }
    }

    @Override
    public BoletoEntidad editarBoleto(EditarBoletoDTO registro) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE boleto 
                                  SET numero_boleto = ?,
                                      codigo_boleto = ?,
                                      precio = ?,
                                      id_evento = ?, 
                                      id_compra = ?
                                  WHERE id_boleto = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);


            comando.setString(1, registro.getNumBoleto());
            comando.setString(2, registro.getCodigoBoleto());
            comando.setDouble(3, registro.getPrecio());
            comando.setInt(4, registro.getIdEvento());
            comando.setInt(5, registro.getIdCompra());
            comando.setInt(6, registro.getId());

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar boleto: " + e.getMessage());
        }
    }

    @Override
    public BoletoEntidad eliminarBoleto(int id) throws PersistenciaException {
        BoletoEntidad boleto = BuscarPorID(id);
        if (boleto == null) {
            throw new PersistenciaException("No existe el boleto con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM boleto WHERE id_boleto = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return boleto;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar boleto: " + e.getMessage());
        }
    }

    @Override
    public BoletoEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_boleto,
                                    numero_boleto,
                                    codigo_boleto,
                                    precio,
                                    id_evento,
                                    id_compra
                                  FROM boleto 
                                  WHERE id_boleto = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            if (rs.next()) {
                return new BoletoEntidad(
                        rs.getInt("id_boleto"),
                        rs.getString("numero_boleto"),
                        rs.getString("codigo_boleto"),
                        rs.getDouble("precio"),
                        rs.getInt("id_evento"),
                        rs.getInt("id_compra"));
            }

            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar boleto: " + e.getMessage());
        }
    }

    @Override
    public List<BoletoEntidad> listarBoletos(String filtro) throws PersistenciaException {
        List<BoletoEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_boleto,
                                    numero_boleto,
                                    codigo_boleto,
                                    precio,
                                    id_evento,
                                    id_compra
                                  FROM boleto 
                                  WHERE numero_boleto LIKE ? OR codigo_boleto LIKE ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                lista.add(new BoletoEntidad(
                        rs.getInt("id_boleto"),
                        rs.getString("numero_boleto"),
                        rs.getString("codigo_boleto"),
                        rs.getDouble("precio"),
                        rs.getInt("id_evento"),
                        rs.getInt("id_compra")
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar boletos: " + e.getMessage());
        }
    }
    
}

//PruebaGit