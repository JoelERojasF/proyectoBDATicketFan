/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.AdministradorEntidad;
import dto.EditarAdministradorDTO;
import dto.GuardarAdministradorDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class AdministradorDAO implements IAdministradorDAO{
    
    private IConexionBD conexion;

    public AdministradorDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public AdministradorEntidad guardarAdministrador(GuardarAdministradorDTO registro) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public AdministradorEntidad editarAdministrador(EditarAdministradorDTO registro) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public AdministradorEntidad eliminarAdministrador(int id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public AdministradorEntidad BuscarPorID(int id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<AdministradorEntidad> listarAlumnos(String filtro) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
