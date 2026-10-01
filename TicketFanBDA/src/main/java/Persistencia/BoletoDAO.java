/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.BoletoEntidad;
import dto.BoletoPDFDTO;
import dto.EditarBoletoDTO;
import dto.GuardarBoletoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class BoletoDAO implements IBoletoDAO{
    
    private IConexionBD conexion;

    public BoletoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    private boolean validarCodigoDisponible(String Codigo) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
                String sentenciaSQL = """
                                  SELECT 1 FROM boleto WHERE codigo_boleto = ? LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, Codigo);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar codigo de boleto: " + e.getMessage());
        }
    }
    
    public boolean boletoYaComprado(int idBoleto) throws PersistenciaException {
        String sentenciaSQL = """
                              SELECT 1
                              FROM boleto
                              WHERE id_boleto = ?
                                AND id_compra IS NOT NULL
                              LIMIT 1
                              """;
        try (Connection con = this.conexion.crearConexion();
             PreparedStatement comando = con.prepareStatement(sentenciaSQL)) {
            comando.setInt(1, idBoleto);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al verificar si el boleto ya fue comprado: " + e.getMessage());
        }
    }

    /**
     * Datos para imprimir UN boleto en PDF. Devuelve null si el boleto no existe,
     * no ha sido comprado o su compra esta cancelada.
     */
    @Override
    public BoletoPDFDTO obtenerDatosBoletoPDF(int idBoleto) throws PersistenciaException {
        String sentenciaSQL = """
                              SELECT c.fecha_hora,
                                     cl.nombres, cl.apellido_paterno, cl.apellido_materno,
                                     b.numero_boleto, b.codigo_boleto, b.precio,
                                     e.nombre_show, e.tipo, e.edad_minima
                              FROM boleto b
                              INNER JOIN compra c  ON b.id_compra = c.id_compra
                              INNER JOIN cliente cl ON c.id_cliente = cl.id_cliente
                              INNER JOIN evento e  ON b.id_evento = e.id_evento
                              WHERE b.id_boleto = ?
                                AND (c.estatus IS NULL OR c.estatus <> 'cancelado')
                              """;
        try (Connection con = this.conexion.crearConexion();
             PreparedStatement comando = con.prepareStatement(sentenciaSQL)) {
            comando.setInt(1, idBoleto);
            try (ResultSet rs = comando.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String nombreCliente = (nullAVacio(rs.getString("nombres")) + " "
                        + nullAVacio(rs.getString("apellido_paterno")) + " "
                        + nullAVacio(rs.getString("apellido_materno"))).trim().replaceAll("\\s+", " ");
                String tipo = rs.getString("tipo");

                BoletoPDFDTO boleto = new BoletoPDFDTO();
                boleto.setNombreCliente(nombreCliente);
                boleto.setNombreEvento(rs.getString("nombre_show"));
                boleto.setTipoEvento(tipo == null || tipo.isBlank() ? "No especificado" : tipo);
                boleto.setEdadMinima(rs.getInt("edad_minima"));
                boleto.setNumeroBoleto(rs.getString("numero_boleto"));
                boleto.setCodigoBoleto(rs.getString("codigo_boleto"));
                boleto.setPrecio(rs.getDouble("precio"));
                boleto.setFechaCompra(rs.getTimestamp("fecha_hora").toLocalDateTime());
                return boleto;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener los datos del boleto: " + e.getMessage());
        }
    }

    private static String nullAVacio(String texto) {
        return texto == null ? "" : texto;
    }

    /** Boletos asociados a una compra (para que el usuario escoja cual imprimir). */
    @Override
    public List<BoletoEntidad> listarBoletosDeCompra(int idCompra) throws PersistenciaException {
        List<BoletoEntidad> lista = new ArrayList<>();
        String sentenciaSQL = """
                              SELECT id_boleto, numero_boleto, codigo_boleto, precio, id_evento, id_compra
                              FROM boleto
                              WHERE id_compra = ?
                              ORDER BY id_boleto
                              """;
        try (Connection con = this.conexion.crearConexion();
             PreparedStatement comando = con.prepareStatement(sentenciaSQL)) {
            comando.setInt(1, idCompra);
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    lista.add(new BoletoEntidad(
                            rs.getInt("id_boleto"),
                            rs.getString("numero_boleto"),
                            rs.getString("codigo_boleto"),
                            rs.getDouble("precio"),
                            rs.getInt("id_evento"),
                            rs.getObject("id_compra", Integer.class)));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los boletos de la compra: " + e.getMessage());
        }
    }

    @Override
    public BoletoEntidad guardarBoleto(GuardarBoletoDTO registro) throws PersistenciaException {
        if(!validarCodigoDisponible(registro.getCodigoBoleto())) throw new PersistenciaException("Error codigo de boleto ya registrado");
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
            if (registro.getIdCompra() == null) {
                comando.setNull(5, Types.INTEGER);
            } else {
                comando.setInt(5, registro.getIdCompra());
            }
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
                        rs.getObject("id_compra", Integer.class));
            }

            throw new PersistenciaException("No existe el boleto con id: " + id);
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
                        rs.getObject("id_compra", Integer.class)
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar boletos: " + e.getMessage());
        }
    }
    
}
