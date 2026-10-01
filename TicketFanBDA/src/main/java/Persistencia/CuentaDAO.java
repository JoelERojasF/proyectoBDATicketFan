/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.CuentaEntidad;
import dto.EditarCuentaDTO;
import dto.GuardarCuentaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author le0jx
 */
public class CuentaDAO implements ICuentaDAO{
    
    private IConexionBD conexion;

    public CuentaDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    /** Disponible = ninguna OTRA cuenta (id distinto a idExcluir) usa ese numero. Para guardar se pasa 0. */
    private boolean validarNumCuentaDisponible(String numCuenta, int idExcluir) throws PersistenciaException{
        try (Connection conexion = this.conexion.crearConexion()) {
                String sentenciaSQL = """
                                  SELECT 1 FROM cuenta WHERE num_cuenta = ? AND id_cuenta <> ? LIMIT 1
                                  """;
                PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
                comando.setString(1, numCuenta);
                comando.setInt(2, idExcluir);
                try (ResultSet rs = comando.executeQuery()) {
                    return !rs.next();
                }
        }catch (SQLException e) {
            throw new PersistenciaException("Error al validar numero de cuenta: " + e.getMessage());
        }
    }
     
    /** null significa "sin dueno de este tipo": se guarda NULL en la BD. */
    private void setIdOpcional(PreparedStatement comando, int indice, Integer valor) throws SQLException {
        if (valor == null) {
            comando.setNull(indice, Types.INTEGER);
        } else {
            comando.setInt(indice, valor);
        }
    }

    private boolean validarCuentaAsociada(Integer idCliente, Integer idPromotora) throws PersistenciaException{
        if(idCliente == null && idPromotora == null)throw new PersistenciaException("Error: la cuenta debe pertenecer a alguien");
        if(idCliente != null && idPromotora != null)throw new PersistenciaException("Error: la cuenta solo puede pertenecer a un solo cliente o a una sola promotora");
        return true;
    }

    @Override
    public CuentaEntidad guardarCuenta(GuardarCuentaDTO registro) throws PersistenciaException {
        if(!validarNumCuentaDisponible(registro.getNumCuenta(), 0)) throw new PersistenciaException("Error numero de cuenta ya registrado");
        validarCuentaAsociada(registro.getIdCliente(), registro.getIdPromotora());
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  INSERT INTO cuenta (banco,
                                                      num_cuenta,
                                                      id_cliente,
                                                      id_promotora) 
                                  VALUES (?,?,?,?);
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);


            comando.setString(1, registro.getBanco());
            comando.setString(2, registro.getNumCuenta());
            setIdOpcional(comando, 3, registro.getIdCliente());
            setIdOpcional(comando, 4, registro.getIdPromotora());

            comando.executeUpdate();

            ResultSet llavesGeneradas = comando.getGeneratedKeys();
            if (llavesGeneradas.next()) {
                int idGenerado = llavesGeneradas.getInt(1);
                return BuscarPorID(idGenerado);
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar cuenta: " + e.getMessage());
        }
    }

    @Override
    public CuentaEntidad editarCuenta(EditarCuentaDTO registro) throws PersistenciaException {
        if(!validarNumCuentaDisponible(registro.getNumCuenta(), registro.getId())) throw new PersistenciaException("Error numero de cuenta ya registrado");
        validarCuentaAsociada(registro.getIdCliente(), registro.getIdPromotora());
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  UPDATE cuenta 
                                  SET saldo = ?,
                                      banco = ?,
                                      num_cuenta = ?,
                                      id_cliente = ?,
                                      id_promotora = ?
                                  WHERE id_cuenta = ? 
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            comando.setDouble(1, registro.getSaldo());
            comando.setString(2, registro.getBanco());
            comando.setString(3, registro.getNumCuenta());
            setIdOpcional(comando, 4, registro.getIdCliente());
            setIdOpcional(comando, 5, registro.getIdPromotora());
            comando.setInt(6, registro.getId());

            int filas = comando.executeUpdate();
            return filas == 1 ? BuscarPorID(registro.getId()) : null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al editar cuenta: " + e.getMessage());
        }
    }

    @Override
    public CuentaEntidad eliminarCuenta(int id) throws PersistenciaException {
        CuentaEntidad cuenta = BuscarPorID(id);
        if (cuenta == null) {
            throw new PersistenciaException("No existe la cuenta con id: " + id);
        }
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = "DELETE FROM cuenta WHERE id_cuenta = ?";

            PreparedStatement comando = conexion.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();

            return cuenta;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar cuenta: " + e.getMessage());
        }
    }

    @Override
    public CuentaEntidad BuscarPorID(int id) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_cuenta,
                                    saldo,
                                    banco,
                                    num_cuenta,
                                    id_cliente,
                                    id_promotora
                                  FROM cuenta 
                                  WHERE id_cuenta = ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);
            comando.setInt(1, id);

            ResultSet rs = comando.executeQuery();
            if (rs.next()) {
                return new CuentaEntidad(
                        rs.getInt("id_cuenta"),
                        rs.getDouble("saldo"),
                        rs.getString("banco"),
                        rs.getString("num_cuenta"),
                        rs.getObject("id_cliente", Integer.class),
                        rs.getObject("id_promotora", Integer.class));
            }

            throw new PersistenciaException("No existe la cuenta con id: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar cuenta: " + e.getMessage());
        }
    }

    @Override
    public List<CuentaEntidad> listarCuentas(String filtro) throws PersistenciaException {
        List<CuentaEntidad> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sentenciaSQL = """
                                  SELECT 
                                    id_cuenta,
                                    saldo,
                                    banco,
                                    num_cuenta,
                                    id_cliente,
                                    id_promotora
                                  FROM cuenta 
                                  WHERE banco LIKE ? OR num_cuenta LIKE ?
                                  """;
            PreparedStatement comando = conexion.prepareStatement(sentenciaSQL);

            String comodinBusqueda = "%" + filtro + "%";
            comando.setString(1, comodinBusqueda);
            comando.setString(2, comodinBusqueda);

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                lista.add(new CuentaEntidad(
                        rs.getInt("id_cuenta"),
                        rs.getDouble("saldo"),
                        rs.getString("banco"),
                        rs.getString("num_cuenta"),
                        rs.getObject("id_cliente", Integer.class),
                        rs.getObject("id_promotora", Integer.class)
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar cuentas: " + e.getMessage());
        }
    }
    
}
