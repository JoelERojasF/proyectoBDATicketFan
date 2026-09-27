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
    CuentaEntidad eliminarCuenta(int id) throws PersistenciaException;
    CuentaEntidad BuscarPorID(int id) throws PersistenciaException;
    List<CuentaEntidad> listarCuentas(String filtro) throws PersistenciaException;
}
