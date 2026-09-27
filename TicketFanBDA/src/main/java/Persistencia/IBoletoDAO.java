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
    BoletoEntidad eliminarBoleto(int id) throws PersistenciaException;
    BoletoEntidad BuscarPorID(int id) throws PersistenciaException;
    List<BoletoEntidad> listarBoletos(String filtro) throws PersistenciaException;
}
