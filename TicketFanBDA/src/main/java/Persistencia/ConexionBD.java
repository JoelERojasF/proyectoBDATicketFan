/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author le0jx
 */
public class ConexionBD implements IConexionBD {

    private final String SERVER = "127.0.0.1";
    private final String PUERTO = "3306";
    private final String BASE_DATOS = "ticketfan";
    private final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + ":" + PUERTO + "/" + BASE_DATOS;
    private final String USUARIO = "root";
    private final String CONTRASEÑA = "RojF#2339";

    @Override
    public Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
    }
}

