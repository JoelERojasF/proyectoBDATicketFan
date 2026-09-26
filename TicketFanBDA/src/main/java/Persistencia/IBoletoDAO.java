/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.BoletoEntidad;
import dto.EditarBoletoDTO;
import dto.GuardarBoletoDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface IBoletoDAO {
    BoletoEntidad guardarBoleto(GuardarBoletoDTO registro) throws PersistenciaException;
    BoletoEntidad editarBoleto(EditarBoletoDTO registro) throws PersistenciaException;
    BoletoEntidad eliminarBoleto(String id) throws PersistenciaException;
    BoletoEntidad BuscarPorID(String id) throws PersistenciaException;
    List<BoletoEntidad> listarAlumnos(String filtro) throws PersistenciaException;
}
