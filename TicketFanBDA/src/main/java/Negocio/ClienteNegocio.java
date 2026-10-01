/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.ClienteEntidad;
import Persistencia.ClienteDAO;
import Persistencia.PersistenciaException;
import dto.EditarClienteDTO;
import dto.GuardarClienteDTO;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class ClienteNegocio {
    
    private final ClienteDAO clienteDAO;
    
    private AdministradorNegocio administradorN;

    public ClienteNegocio(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void setAdministradorN(AdministradorNegocio administradorN) {
        this.administradorN = administradorN;
    }
    
    /** El usuario tampoco puede coincidir con el de un administrador. */
    private void validarUsuarioLibreEnAdministradores(String usuario) throws NegocioException {
        try {
            if(!administradorN.getAdministradorDAO().validarUsuarioDisponible(usuario)) throw new NegocioException("El nombre de usuario del cliente ya registrado.");
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al validar usuario del cliente: " + e.getMessage());
        }
    }
    
    public ClienteEntidad guardarCliente(String nombres, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, String usuario, String contraseña) throws NegocioException{
        if(!Validaciones.validarNombrePersona(nombres)) throw new NegocioException("El nombre del cliente es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoPaterno)) throw new NegocioException("El apellido paterno del cliente es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoMaterno)) throw new NegocioException("El apellido materno del cliente es invalido.");
        if(!Validaciones.validarFechaAntes(fechaNacimiento)) throw new NegocioException("La fecha de nacimiento del cliente es invalida.");
        if(!Validaciones.validarTexto(usuario)) throw new NegocioException("El nombre de usuario del cliente es invalido.");
        validarUsuarioLibreEnAdministradores(usuario);
        if(!Validaciones.validarContraseña(contraseña)) throw new NegocioException("La contraseña del cliente es invalida.");
        
        try{
            GuardarClienteDTO registro = new GuardarClienteDTO(nombres, apellidoPaterno, apellidoMaterno, fechaNacimiento ,usuario, contraseña);
            return clienteDAO.guardarCliente(registro);
        }catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar cliente: " + e.getMessage());
        }
    }
    
    public ClienteEntidad editarCliente(String id, String nombres, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, String usuario, String contraseña) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del cliente es invalido.");
        if(!Validaciones.validarNombrePersona(nombres)) throw new NegocioException("El nombre del cliente es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoPaterno)) throw new NegocioException("El apellido paterno del cliente es invalido.");
        if(!Validaciones.validarNombrePersona(apellidoMaterno)) throw new NegocioException("El apellido materno del cliente es invalido.");
        if(!Validaciones.validarFechaAntes(fechaNacimiento)) throw new NegocioException("La fecha de nacimiento del cliente es invalida.");
        if(!Validaciones.validarTexto(usuario)) throw new NegocioException("El nombre de usuario del cliente es invalido.");
        validarUsuarioLibreEnAdministradores(usuario);
        if(!Validaciones.validarContraseña(contraseña)) throw new NegocioException("La contraseña del cliente es invalida.");
        
        try {
            EditarClienteDTO registro = new EditarClienteDTO(Integer.parseInt(id.trim()), nombres, apellidoPaterno, apellidoMaterno, fechaNacimiento ,usuario, contraseña);
            return clienteDAO.editarCliente(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al editar cliente: " + e.getMessage());
        }
    }
    
    public ClienteEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del cliente es invalido.");
        try {
            return clienteDAO.BuscarPorID(Integer.parseInt(id.trim()));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar cliente: " + e.getMessage());
        }
    }
    
    public ClienteEntidad eliminarCliente(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del cliente es invalido.");
        try {
            return clienteDAO.eliminarCliente(Integer.parseInt(id.trim()));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar cliente: " + e.getMessage());
        }
    }
    
    public List<ClienteEntidad> listarClientes(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return clienteDAO.listarClientes(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar clientes: " + e.getMessage());
        }
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }
}
