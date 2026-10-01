/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.AdministradorEntidad;
import Negocio.NegocioException;
import Persistencia.AdministradorDAO;
import Persistencia.PersistenciaException;
import dto.EditarAdministradorDTO;
import dto.GuardarAdministradorDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class AdministradorNegocio {
    
    private final AdministradorDAO administradorDAO;
    
    private PromotoraNegocio promotoraN;
    private ClienteNegocio clienteN;
    

    public AdministradorNegocio(AdministradorDAO administradorDAO) {
        this.administradorDAO = administradorDAO;
    }

    public void setPromotoraN(PromotoraNegocio promotoraN) {
        this.promotoraN = promotoraN;
    }

    public void setClienteN(ClienteNegocio clienteN) {
        this.clienteN = clienteN;
    }
    
    /** El usuario tampoco puede coincidir con el de un cliente. */
    private void validarUsuarioLibreEnClientes(String usuario) throws NegocioException {
        try {
            if(!clienteN.getClienteDAO().validarUsuarioDisponible(usuario)) throw new NegocioException("El nombre de usuario del administrador ya registrado.");
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al validar usuario del administrador: " + e.getMessage());
        }
    }
    
    public AdministradorEntidad guardarAdministrador(String nombres, String apellidoPaterno, String apellidoMaterno, String usuario, String contraseña) throws NegocioException{
        if(!Validaciones.validarNombrePersona(nombres)) throw new NegocioException("El nombre del administrador es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoPaterno)) throw new NegocioException("El apellido paterno del administrador es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoMaterno)) throw new NegocioException("El apellido materno del administrador es invalido.");
        if(!Validaciones.validarTexto(usuario)) throw new NegocioException("El nombre de usuario del administrador es invalido.");
        validarUsuarioLibreEnClientes(usuario);
        if(!Validaciones.validarContraseña(contraseña)) throw new NegocioException("La contraseña del administrador es invalida.");
        
        try{
            GuardarAdministradorDTO registro = new GuardarAdministradorDTO(nombres, apellidoPaterno, apellidoMaterno, usuario, contraseña);
            return administradorDAO.guardarAdministrador(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar administrador: " + e.getMessage());
        }
    }
    
    public AdministradorEntidad editarAdministrador(String id, String nombres, String apellidoPaterno, String apellidoMaterno, String usuario, String contraseña, String idPromotora) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del administrador es invalido.");
        if(!Validaciones.validarNombrePersona(nombres)) throw new NegocioException("El nombre del administrador es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoPaterno)) throw new NegocioException("El apellido paterno del administrador es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoMaterno)) throw new NegocioException("El apellido materno del administrador es invalido.");
        if(!Validaciones.validarTexto(usuario)) throw new NegocioException("El nombre de usuario del administrador es invalido.");
        validarUsuarioLibreEnClientes(usuario);
        if(!Validaciones.validarContraseña(contraseña)) throw new NegocioException("La contraseña del administrador es invalida.");
        if(!Validaciones.validarPositivo(idPromotora)) throw new NegocioException("El id buscado de la promotora es invalido.");
        if(promotoraN.BuscarPorID(idPromotora) == null) throw new NegocioException("El id buscado de la promotora no existe.");
        try{
            EditarAdministradorDTO registro = new EditarAdministradorDTO(Integer.parseInt(id), nombres, apellidoPaterno, apellidoMaterno, usuario, contraseña, Integer.parseInt(idPromotora));
            return administradorDAO.editarAdministrador(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al editar administrador: " + e.getMessage());
        }
    }
    
    public AdministradorEntidad eliminarAdministrador(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del administrador es invalido.");
        try {
            return administradorDAO.eliminarAdministrador(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar administrador: " + e.getMessage());
        }
    }
    
    public AdministradorEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del administrador es invalido.");
        try {
            return administradorDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar administrador: " + e.getMessage());
        }
    }
    
    public List<AdministradorEntidad> listarAdministradores(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return administradorDAO.listarAdministradores(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar administradores: " + e.getMessage());
        }
    }

    public AdministradorDAO getAdministradorDAO() {
        return administradorDAO;
    }
    
    
}
