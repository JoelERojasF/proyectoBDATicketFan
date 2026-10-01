/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.PromotoraEntidad;
import Persistencia.PersistenciaException;
import Persistencia.PromotoraDAO;
import dto.EditarPromotoraDTO;
import dto.GuardarPromotoraDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class PromotoraNegocio {
    
    private final PromotoraDAO promotoraDAO;

    public PromotoraNegocio(PromotoraDAO promotora) {
        this.promotoraDAO = promotora;
    }
    
    public PromotoraEntidad guardarPromotora(String nombre, String colonia, String calle, String numero, String ciudad, String estado) throws NegocioException{
        if(!Validaciones.validarTexto(nombre)) throw new NegocioException("El nombre de la promotora es invalido.");
        if(!Validaciones.validarTexto(colonia)) throw new NegocioException("El colonia de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(calle)) throw new NegocioException("La calle de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(numero)) throw new NegocioException("El numero de la direccion de la promotora es invalido.");
        if(!Validaciones.validarTexto(ciudad)) throw new NegocioException("La ciudad de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(estado)) throw new NegocioException("El estado de la direccion de la promotora es invalido.");
        
        try{
            GuardarPromotoraDTO registro = new GuardarPromotoraDTO(nombre, colonia, calle, numero, ciudad, estado);
            return promotoraDAO.guardarPromotora(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar la promotora: " + e.getMessage());
        }
    }
    
    public PromotoraEntidad editarPromotora(String id, String nombre, String colonia, String calle, String numero, String ciudad, String estado) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la promotora es invalido.");
        if(!Validaciones.validarTexto(nombre)) throw new NegocioException("El nombre de la promotora es invalido.");
        if(!Validaciones.validarTexto(colonia)) throw new NegocioException("El colonia de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(calle)) throw new NegocioException("La calle de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(numero)) throw new NegocioException("El numero de la direccion de la promotora es invalido.");
        if(!Validaciones.validarTexto(ciudad)) throw new NegocioException("La ciudad de la direccion de la promotora es invalida.");
        if(!Validaciones.validarTexto(estado)) throw new NegocioException("El estado de la direccion de la promotora es invalido.");
        
    try{
            EditarPromotoraDTO registro = new EditarPromotoraDTO(Integer.parseInt(id), nombre, colonia, calle, numero, ciudad, estado);
            return promotoraDAO.editarPromotora(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al editar la promotora: " + e.getMessage());
        }
    }
    
    public PromotoraEntidad eliminarPromotora(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la promotora es invalido.");
        try {
            return promotoraDAO.eliminarPromotora(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar la promotora: " + e.getMessage());
        }
    }
    
    public PromotoraEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado de la promotora es invalido.");
        try {
            return promotoraDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar la promotora: " + e.getMessage());
        }
    }
    
    public List<PromotoraEntidad> listarPromotoras(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return promotoraDAO.listarPromotoras(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar las promotoras: " + e.getMessage());
        }
    }
    
}
