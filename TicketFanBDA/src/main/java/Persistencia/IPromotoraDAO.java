/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.PromotoraEntidad;
import dto.EditarPromotoraDTO;
import dto.GuardarPromotoraDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface IPromotoraDAO {
    PromotoraEntidad guardarPromotora(GuardarPromotoraDTO registro) throws PersistenciaException;
    PromotoraEntidad editarPromotora(EditarPromotoraDTO registro) throws PersistenciaException;
    PromotoraEntidad eliminarPromotora(String id) throws PersistenciaException;
    PromotoraEntidad BuscarPorID(String id) throws PersistenciaException;
    List<PromotoraEntidad> listarAlumnos(String filtro) throws PersistenciaException;
}
