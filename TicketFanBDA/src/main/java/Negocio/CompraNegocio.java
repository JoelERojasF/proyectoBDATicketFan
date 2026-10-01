/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.CompraEntidad;
import Entidades.CuentaEntidad;
import Persistencia.CompraDAO;
import Persistencia.PersistenciaException;
import dto.EditarCompraDTO;
import dto.GuardarCompraDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class CompraNegocio {
    
    private final CompraDAO compraDAO;
    
    private ClienteNegocio clienteN;
    private CuentaNegocio cuentaN;

    public CompraNegocio(CompraDAO compraDAO) {
        this.compraDAO = compraDAO;
    }
    
    public CompraEntidad cancelarCompra(String id) throws NegocioException, PersistenciaException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la compra es invalido.");
        try{
            CompraEntidad compra = compraDAO.BuscarPorID(Integer.parseInt(id));
            LocalDateTime limite = compra.getFechaHora().plusDays(1);
        
            if(LocalDateTime.now().isAfter(limite)) throw new NegocioException("Error: la cancelacion de una compra solo puede ocurrir dentro de las siguientes 24 horas desde que se realizo la compra");
        
            EditarCompraDTO cancelacion = new EditarCompraDTO(compra.getId(), compra.getDetalles(), compra.getTotal(), "cancelado", compra.getFechaHora(), compra.getIdCliente(), compra.getIdCuenta());
            compraDAO.editarCompra(cancelacion);
            CuentaEntidad cuenta = cuentaN.BuscarPorID(compra.getIdCuenta()+"");
            cuentaN.editarCuenta(cuenta.getId()+"", (cuenta.getSaldo()+compra.getTotal())+"", cuenta.getBanco(), cuenta.getNumCuenta(), cuenta.getIdCliente()+"", "0");
            return compra;
        }catch(PersistenciaException e){
            throw new NegocioException("Error al cancelar compra: " + e.getMessage());
        }
    }
    
    
    
    public CompraEntidad guardarCompra(String detalles, String total, LocalDateTime fechaHora, String idCliente, String idCuenta) throws NegocioException{
        if(!Validaciones.validarTexto(detalles))throw new NegocioException("El detalle de la compra es invalido.");
        if(!Validaciones.validarCantidadDinero(total)) throw new NegocioException("EL total de la compra invalido");
        if(!Validaciones.validarPositivo(idCliente)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(clienteN.BuscarPorID(idCliente) == null) throw new NegocioException("El id buscado del cliente no existe.");
        if(!Validaciones.validarPositivo(idCuenta)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        if(cuentaN.BuscarPorID(idCuenta) == null) throw new NegocioException("El id buscado de la cuenta no existe.");        
        try{
            GuardarCompraDTO registro = new GuardarCompraDTO(detalles, Double.parseDouble(total), fechaHora, Integer.parseInt(idCliente), Integer.parseInt(idCuenta));
            return compraDAO.guardarCompra(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar administrador: " + e.getMessage());
        }
    }
    
    public CompraEntidad editarCompra(String id, String detalles, String total, String estatus, LocalDateTime fechaHora, String idCliente, String idCuenta) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la compra es invalido.");
        if(!Validaciones.validarTexto(detalles))throw new NegocioException("El detalle de la compra es invalido.");
        if(!Validaciones.validarCantidadDinero(total)) throw new NegocioException("EL total de la compra invalido");
        if(!Validaciones.validarStatus(estatus)) throw new NegocioException("EL estatus de la compra invalido");
        if(!Validaciones.validarPositivo(idCliente)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(clienteN.BuscarPorID(idCliente) == null) throw new NegocioException("El id buscado del cliente no existe.");
        if(!Validaciones.validarPositivo(idCuenta)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        if(cuentaN.BuscarPorID(idCuenta) == null) throw new NegocioException("El id buscado de la cuenta no existe."); 
        try{
            EditarCompraDTO registro = new EditarCompraDTO(Integer.parseInt(id), detalles, Double.parseDouble(total), estatus, fechaHora, Integer.parseInt(idCliente), Integer.parseInt(idCuenta));
            return compraDAO.editarCompra(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al editar administrador: " + e.getMessage());
        }
    }
    
    public CompraEntidad eliminarAdministrador(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la compra es invalido.");
        try {
            return compraDAO.eliminarCompra(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar compras: " + e.getMessage());
        }
    }
    
    public CompraEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la compra es invalido.");
        try {
            return compraDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar compras: " + e.getMessage());
        }
    }
    
    public List<CompraEntidad> listarAdministradores(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return compraDAO.listarCompras(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar compras: " + e.getMessage());
        }
    }
}
