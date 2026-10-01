/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author le0jx
 */
public class Validaciones {
    public static boolean validarNombrePersona(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]{1,50}$";
        return texto.trim().matches(patron);
    }
    
    public static boolean validarTexto(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9]{1,50}$";
        return texto.trim().matches(patron);
    }
    
    public static boolean validarContraseña(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        if(texto.length() >= 4) return true;
        return false;
    }
    
    public boolean validarLogin(String contraseñaIngresada, String hashGuardado) {
        return BCrypt.checkpw(contraseñaIngresada, hashGuardado);
    }
    
    public static boolean validarStatus(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        if(texto.equalsIgnoreCase("cancelado") || texto.equalsIgnoreCase("comprado")) return true;
        return false;
    }
    
    public static boolean validarPositivo(String texto) {
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[0-9][0-9]*$";
        return texto.trim().matches(patron);
    }
    
    public static boolean validarFechaAntes(LocalDate fecha){
        LocalDate hoy = LocalDate.now();
        
        return fecha.isBefore(hoy);
    }
    
    public static boolean validarNumCuenta(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[1-9][0-9]{9}$";
        return texto.trim().matches(patron);
    }
    
    public static boolean validarCantidadDinero(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[1-9][0-9]{0,7}(\\.[0-9]{1,2})?$";
        return texto.trim().matches(patron);
    }
}
