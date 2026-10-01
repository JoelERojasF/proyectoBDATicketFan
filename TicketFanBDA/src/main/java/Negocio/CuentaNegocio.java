/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.CuentaEntidad;
import Persistencia.CuentaDAO;
import Persistencia.PersistenciaException;
import dto.EditarCuentaDTO;
import dto.GuardarCuentaDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class CuentaNegocio {
    
    private final CuentaDAO cuentaDAO;
    
    private PromotoraNegocio promotoraN;
    private ClienteNegocio clienteN;

    public CuentaNegocio(CuentaDAO cuentaDAO) {
        this.cuentaDAO = cuentaDAO;
    }

    public void setPromotoraN(PromotoraNegocio promotoraN) {
        this.promotoraN = promotoraN;
    }

    public void setClienteN(ClienteNegocio clienteN) {
        this.clienteN = clienteN;
    }
    
    /**
     * Una cuenta pertenece a UN cliente o a UNA promotora (nunca a ambos ni a ninguno).
     * Para el tipo de dueño que no aplica se deja el id vacio (null).
     */
    private void validarDueno(String idCliente, String idPromotora) throws NegocioException {
        if(!Validaciones.validarIdOpcional(idCliente)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(!Validaciones.validarIdOpcional(idPromotora)) throw new NegocioException("El id buscado de la promotora es invalido.");
        boolean hayCliente = !Validaciones.esVacio(idCliente);
        boolean hayPromotora = !Validaciones.esVacio(idPromotora);
        if(hayCliente == hayPromotora) throw new NegocioException("La cuenta debe pertenecer a un cliente o a una promotora, pero no a ambos.");
        if(hayCliente && clienteN.BuscarPorID(idCliente) == null) throw new NegocioException("El id buscado del cliente no existe.");
        if(hayPromotora && promotoraN.BuscarPorID(idPromotora) == null) throw new NegocioException("El id buscado de la promotora no existe.");
    }
    
    private static Integer idOpcional(String texto) {
        return Validaciones.esVacio(texto) ? null : Integer.valueOf(texto.trim());
    }
    
    public CuentaEntidad guardarCuenta(String banco, String numCuenta, String idCliente, String idPromotora) throws NegocioException{
        if(!Validaciones.validarTexto(banco)) throw new NegocioException("El nombre de banco de la cuenta es invalido.");
        if(!Validaciones.validarNumCuenta(numCuenta)) throw new NegocioException("El numero de la cuenta es invalido.");
        validarDueno(idCliente, idPromotora);

        try{
            GuardarCuentaDTO registro = new GuardarCuentaDTO(banco, numCuenta, idOpcional(idCliente), idOpcional(idPromotora));
            return cuentaDAO.guardarCuenta(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar cuenta: " + e.getMessage());
        }
    }
    
    public CuentaEntidad editarCuenta(String id, String saldo, String banco, String numCuenta, String idCliente, String idPromotora) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        if(!Validaciones.validarCantidadDinero(saldo)) throw new NegocioException("El saldo de la cuenta invalido");
        if(!Validaciones.validarTexto(banco)) throw new NegocioException("El nombre de banco de la cuenta es invalido.");
        if(!Validaciones.validarNumCuenta(numCuenta)) throw new NegocioException("El numero de la cuenta es invalido.");
        validarDueno(idCliente, idPromotora);

        try{
            EditarCuentaDTO registro = new EditarCuentaDTO(Integer.parseInt(id),Double.parseDouble(saldo), banco, numCuenta, idOpcional(idCliente), idOpcional(idPromotora));
            return cuentaDAO.editarCuenta(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al editar cuenta: " + e.getMessage());
        }
    }
    
    
    public CuentaEntidad eliminarCuenta(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        try {
            return cuentaDAO.eliminarCuenta(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar cuenta: " + e.getMessage());
        }
    }
    
    public CuentaEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        try {
            return cuentaDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar cuenta: " + e.getMessage());
        }
    }
    
    public List<CuentaEntidad> listarCuentas(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return cuentaDAO.listarCuentas(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar cuenta: " + e.getMessage());
        }
    }
}
