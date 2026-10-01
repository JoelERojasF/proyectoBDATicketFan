/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.BoletoEntidad;
import Persistencia.BoletoDAO;
import Persistencia.PersistenciaException;
import dto.EditarBoletoDTO;
import dto.GuardarBoletoDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class BoletoNegocio {
    private final BoletoDAO boletoDAO;
    
    private EventoNegocio eventoN;
    private CompraNegocio compraN;

    public BoletoNegocio(BoletoDAO boletoDAO) {
        this.boletoDAO = boletoDAO;
    }
    
    public BoletoEntidad guardarBoleto(String numBoleto, String codigoBoleto, String precio, String idEvento) throws NegocioException{
        if(!Validaciones.validarTexto(numBoleto)) throw new NegocioException("El numero del boleto es invalido.");
        if(!Validaciones.validarTexto(codigoBoleto)) throw new NegocioException("El codigo del boleto es invalido.");
        if(!Validaciones.validarCantidadDinero(precio)) throw new NegocioException("EL precio del boleto es invalido");
        if(!Validaciones.validarPositivo(idEvento)) throw new NegocioException("El id buscado del evento es invalido.");
        if(eventoN.BuscarPorID(idEvento) == null) throw new NegocioException("El id buscado del evento no existe.");

        try {
            GuardarBoletoDTO registro = new GuardarBoletoDTO(numBoleto, codigoBoleto, Double.parseDouble(precio), Integer.parseInt(idEvento));
            return boletoDAO.guardarBoleto(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar administrador: " + e.getMessage());
        }
    }
    
    public BoletoEntidad editarBoleto(String id, String numBoleto, String codigoBoleto, String precio, String idEvento, String idCompra) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        if(!Validaciones.validarTexto(numBoleto)) throw new NegocioException("El numero del boleto es invalido.");
        if(!Validaciones.validarTexto(codigoBoleto)) throw new NegocioException("El codigo del boleto es invalido.");
        if(!Validaciones.validarCantidadDinero(precio)) throw new NegocioException("EL precio del boleto es invalido");
        if(!Validaciones.validarPositivo(idEvento)) throw new NegocioException("El id buscado del evento es invalido.");
        if(eventoN.BuscarPorID(idEvento) == null) throw new NegocioException("El id buscado del evento no existe.");
        if(!Validaciones.validarPositivo(idCompra)) throw new NegocioException("El id buscado de la compra es invalido.");
        if(compraN.BuscarPorID(idCompra) == null) throw new NegocioException("El id buscado de la compra no existe.");
        try {
            EditarBoletoDTO registro = new EditarBoletoDTO(Integer.parseInt(id), numBoleto, codigoBoleto, Double.parseDouble(precio), Integer.parseInt(idEvento), Integer.parseInt(idCompra));
            return boletoDAO.editarBoleto(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al editar administrador: " + e.getMessage());
        }
    }
    
    public BoletoEntidad eliminarAdministrador(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        try {
            return boletoDAO.eliminarBoleto(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar boletos: " + e.getMessage());
        }
    }
    
    public BoletoEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        try {
            return boletoDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar boletos: " + e.getMessage());
        }
    }
    
    public List<BoletoEntidad> listarAdministradores(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return boletoDAO.listarBoletos(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar boletos: " + e.getMessage());
        }
    }
}
