/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.EventoEntidad;
import Persistencia.EventoDAO;
import Persistencia.PersistenciaException;
import dto.EditarEventoDTO;
import dto.GuardarEventoDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class EventoNegocio {
    private final EventoDAO eventoDAO;
    
    private AdministradorNegocio administradorN;
    private CuentaNegocio cuentaN;

    public EventoNegocio(EventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
    }

    public void setAdministradorN(AdministradorNegocio administradorN) {
        this.administradorN = administradorN;
    }

    public void setCuentaN(CuentaNegocio cuentaN) {
        this.cuentaN = cuentaN;
    }
    
    public EventoEntidad guardarEvento(String nombreShow, String cantidadBoletos, String idAdministrador) throws NegocioException{
        if(!Validaciones.validarTexto(nombreShow)) throw new NegocioException("El nombre del evento es invalido.");
        if(!Validaciones.validarPositivo(cantidadBoletos)) throw new NegocioException("El numero de boletos del evento es invalido.");
        if(!Validaciones.validarPositivo(idAdministrador)) throw new NegocioException("El id buscado del administrador es invalido.");
        if(administradorN.BuscarPorID(idAdministrador) == null) throw new NegocioException("El id buscado del administrador no existe.");

        try {
            GuardarEventoDTO registro = new GuardarEventoDTO(nombreShow, Integer.parseInt(cantidadBoletos), Integer.parseInt(idAdministrador));
            return eventoDAO.guardarEvento(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar evento: " + e.getMessage());
        }
    }
    
    public EventoEntidad editarEvento(String id, String nombreShow, String tipo, String edadMinima, String imagenPromocional, String cantidadBoletos, String idAdministrador, String idCuenta) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del evento es invalido.");        
        if(!Validaciones.validarTexto(nombreShow)) throw new NegocioException("El nombre del evento es invalido.");
        if(!Validaciones.validarTexto(tipo)) throw new NegocioException("El tipo del evento es invalido.");
        if(!Validaciones.validarPositivo(edadMinima)) throw new NegocioException("La edad minima del evento es invalida.");  
        if(!Validaciones.validarImagen(imagenPromocional)) throw new NegocioException("La imagen promocional del evento es invalida.");
        if(!Validaciones.validarPositivo(cantidadBoletos)) throw new NegocioException("El numero de boletos del evento es invalido.");
        if(!Validaciones.validarPositivo(idAdministrador)) throw new NegocioException("El id buscado del administrador es invalido.");
        if(administradorN.BuscarPorID(idAdministrador) == null) throw new NegocioException("El id buscado del administrador no existe.");
        if(!Validaciones.validarPositivo(idCuenta)) throw new NegocioException("El id buscado de la cuenta es invalido.");
        if(cuentaN.BuscarPorID(idCuenta) == null) throw new NegocioException("El id buscado de la cuenta no existe.");        
        try {
            EditarEventoDTO registro = new EditarEventoDTO(Integer.parseInt(id), nombreShow, tipo, Integer.parseInt(edadMinima), imagenPromocional, Integer.parseInt(cantidadBoletos), Integer.parseInt(idAdministrador), Integer.parseInt(idCuenta));
            return eventoDAO.editarEvento(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al editar evento: " + e.getMessage());
        }
    }
    
    public EventoEntidad eliminarEvento(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del evento es invalido.");
        try {
            return eventoDAO.eliminarEvento(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar evento: " + e.getMessage());
        }
    }
    
    public EventoEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del evento es invalido.");
        try {
            return eventoDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar evento: " + e.getMessage());
        }
    }
    
    public List<EventoEntidad> listarEventos(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return eventoDAO.listarEventos(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar eventos: " + e.getMessage());
        }
    }
}
