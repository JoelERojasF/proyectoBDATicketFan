/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.CompraEntidad;
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

    public void setClienteN(ClienteNegocio clienteN) {
        this.clienteN = clienteN;
    }

    public void setCuentaN(CuentaNegocio cuentaN) {
        this.cuentaN = cuentaN;
    }
    
    public CompraEntidad cancelarCompra(String id) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la compra es invalido.");
        try{
            CompraEntidad compra = compraDAO.BuscarPorID(Integer.parseInt(id.trim()));
            if("cancelado".equalsIgnoreCase(compra.getEstatus())) throw new NegocioException("Error: la compra ya fue cancelada anteriormente.");
            
            LocalDateTime limite = compra.getFechaHora().plusDays(1);
            if(LocalDateTime.now().isAfter(limite)) throw new NegocioException("Error: la cancelacion de una compra solo puede ocurrir dentro de las siguientes 24 horas desde que se realizo la compra");
        
            // Una sola transaccion: estatus + reembolso al cliente + descuento a la promotora + liberar boletos
            return compraDAO.cancelarCompraConReembolso(compra.getId());
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
            throw new NegocioException("Error al guardar compra: " + e.getMessage());
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
            throw new NegocioException("Error al editar compra: " + e.getMessage());
        }
    }
    
    public CompraEntidad eliminarCompra(String id) throws NegocioException{
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
    
    public List<CompraEntidad> listarCompras(String filtro) throws NegocioException {
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
