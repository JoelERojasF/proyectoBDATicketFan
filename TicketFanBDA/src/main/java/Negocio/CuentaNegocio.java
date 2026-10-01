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
    
    
    public CuentaEntidad guardarCuenta(String banco, String numCuenta, String idCliente, String idPromotora) throws NegocioException{
        if(!Validaciones.validarTexto(banco)) throw new NegocioException("El nombre de banco de la cuenta es invalido.");
        if(!Validaciones.validarNumCuenta(numCuenta)) throw new NegocioException("El numero de la cuenta es invalido.");
        if(!Validaciones.validarPositivo(idCliente)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(!Validaciones.validarPositivo(idPromotora)) throw new NegocioException("El id buscado de la promotora es invalido.");
        if(clienteN.BuscarPorID(idCliente) == null) throw new NegocioException("El id buscado del cliente no existe.");
        if(promotoraN.BuscarPorID(idPromotora) == null) throw new NegocioException("El id buscado de la promotora no existe.");

        try{
            GuardarCuentaDTO registro = new GuardarCuentaDTO(banco, numCuenta, Integer.parseInt(idCliente), Integer.parseInt(idPromotora));
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
        if(!Validaciones.validarPositivo(idCliente)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(!Validaciones.validarPositivo(idPromotora)) throw new NegocioException("El id buscado de la promotora es invalido.");
        if(clienteN.BuscarPorID(idCliente) == null) throw new NegocioException("El id buscado del cliente no existe.");
        if(promotoraN.BuscarPorID(idPromotora) == null) throw new NegocioException("El id buscado de la promotora no existe.");

        try{
            EditarCuentaDTO registro = new EditarCuentaDTO(Integer.parseInt(id),Double.parseDouble(saldo), banco, numCuenta, Integer.parseInt(idCliente), Integer.parseInt(idPromotora));
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
