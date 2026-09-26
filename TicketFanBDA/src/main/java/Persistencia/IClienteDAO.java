/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.ClienteEntidad;
import dto.EditarClienteDTO;
import dto.GuardarClienteDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface IClienteDAO {
    ClienteEntidad guardarCliente(GuardarClienteDTO registro) throws PersistenciaException;
    ClienteEntidad editarCliente(EditarClienteDTO registro) throws PersistenciaException;
    ClienteEntidad eliminarCliente(int id) throws PersistenciaException;
    ClienteEntidad BuscarPorID(int id) throws PersistenciaException;
    List<ClienteEntidad> listarAlumnos(String filtro) throws PersistenciaException;
}
