/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
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
public interface IAdministradorDAO {
    AdministradorEntidad guardarAdministrador(GuardarAdministradorDTO registro) throws PersistenciaException;
    AdministradorEntidad editarAdministrador(EditarAdministradorDTO registro) throws PersistenciaException;
    AdministradorEntidad eliminarAdministrador(String id) throws PersistenciaException;
    AdministradorEntidad BuscarPorID(String id) throws PersistenciaException;
    List<AdministradorEntidad> listarAlumnos(String filtro) throws PersistenciaException;
}
