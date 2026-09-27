/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tutiketbda;

import Persistencia.ConexionBD;
import Persistencia.IConexionBD;
import Persistencia.PersistenciaException;
import Persistencia.PromotoraDAO;
import dto.EditarPromotoraDTO;
import dto.GuardarPromotoraDTO;

/**
 *
 * @author le0jx
 */
public class TuTiketBDA {

    public static void main(String[] args) throws PersistenciaException {
        System.out.println("Hello World!");
        
        ConexionBD conexion = new ConexionBD();
        PromotoraDAO prueba = new PromotoraDAO(conexion);
        GuardarPromotoraDTO GPPueba = new GuardarPromotoraDTO("empresa falsa", "colonia inexistente", "calle inventada", "001", "ciudad fantasma", "estado nulo");
        EditarPromotoraDTO EPprueba = new EditarPromotoraDTO(1, "empresa real", "colonia existente", "calle no inventada", "001", "ciudad viva", "estado no nulo");
        
//        System.out.println(prueba.guardarPromotora(GPPueba).toString());
//        System.out.println(prueba.listarPromotoras("").size());
//        System.out.println(prueba.editarPromotora(EPprueba).toString());
//        System.out.println(prueba.eliminarPromotora(1));
        
    }
}
