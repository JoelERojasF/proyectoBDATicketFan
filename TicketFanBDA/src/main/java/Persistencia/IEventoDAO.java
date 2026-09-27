/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.EventoEntidad;
import dto.EditarEventoDTO;
import dto.GuardarEventoDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface IEventoDAO {
    EventoEntidad guardarEvento(GuardarEventoDTO registro) throws PersistenciaException;
    EventoEntidad editarEvento(EditarEventoDTO registro) throws PersistenciaException;
    EventoEntidad eliminarEvento(int id) throws PersistenciaException;
    EventoEntidad BuscarPorID(int id) throws PersistenciaException;
    List<EventoEntidad> listarEventos(String filtro) throws PersistenciaException;
}
