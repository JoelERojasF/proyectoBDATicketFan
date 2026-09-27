/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.CompraEntidad;
import dto.EditarCompraDTO;
import dto.GuardarCompraDTO;
import java.util.List;

/**
 *
 * @author le0jx
 */
public interface ICompraDAO {
    CompraEntidad guardarCompra(GuardarCompraDTO registro) throws PersistenciaException;
    CompraEntidad editarCompra(EditarCompraDTO registro) throws PersistenciaException;
    CompraEntidad eliminarCompra(int id) throws PersistenciaException;
    CompraEntidad BuscarPorID(int id) throws PersistenciaException;
    List<CompraEntidad> listarCompras(String filtro) throws PersistenciaException;
}
