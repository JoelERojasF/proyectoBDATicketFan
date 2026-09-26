/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.CuentaEntidad;
import dto.EditarCuentaDTO;
import dto.GuardarCuentaDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface ICuentaDAO {
    CuentaEntidad guardarCuenta(GuardarCuentaDTO registro) throws PersistenciaException;
    CuentaEntidad editarCuenta(EditarCuentaDTO registro) throws PersistenciaException;
    CuentaEntidad eliminarCuenta(String id) throws PersistenciaException;
    CuentaEntidad BuscarPorID(String id) throws PersistenciaException;
    List<CuentaEntidad> listarAlumnos(String filtro) throws PersistenciaException;
}
