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
    private static final String LETRAS = "A-Za-zÁÉÍÓÚáéíóúÑñÜü";
    
    /** Letras, permitiendo espacios simples entre palabras (ej. "Maria Jose"). Maximo 50 caracteres. */
    public static boolean validarNombrePersona(String texto){
        if(texto == null || texto.isBlank()) return false;
        
        String limpio = texto.trim();
        String patron = "^[" + LETRAS + "]+( [" + LETRAS + "]+)*$";
        return limpio.length() <= 50 && limpio.matches(patron);
    }
    
    /** Letras y numeros, permitiendo espacios simples entre palabras. Maximo 50 caracteres. */
    public static boolean validarTexto(String texto){
        if(texto == null || texto.isBlank()) return false;
        
        String limpio = texto.trim();
        String patron = "^[" + LETRAS + "0-9]+( [" + LETRAS + "0-9]+)*$";
        return limpio.length() <= 50 && limpio.matches(patron);
    }
    
    /** Ruta o nombre de archivo de imagen (acepta puntos, guiones, diagonales y extension de imagen). */
    public static boolean validarImagen(String texto){
        if(texto == null || texto.isBlank()) return false;
        
        String limpio = texto.trim();
        String patron = "^[\\w\\-. /\\\\:]+\\.(?i:jpg|jpeg|png|gif|webp)$";
        return limpio.length() <= 255 && limpio.matches(patron);
    }
    
    public static boolean validarContraseña(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        if(texto.length() >= 4) return true;
        return false;
    }
    
    public boolean validarLogin(String contraseñaIngresada, String hashGuardado) {
        return BCrypt.checkpw(contraseñaIngresada, hashGuardado);
    }
    
    /** true si el texto es null o esta en blanco (se interpreta como "sin valor"). */
    public static boolean esVacio(String texto){
        return texto == null || texto.isBlank();
    }
    
    /** Id opcional: vacio/null (= sin valor) o un entero positivo valido (1 a 999999999). */
    public static boolean validarIdOpcional(String texto){
        if(esVacio(texto)) return true;
        return texto.trim().matches("^[1-9][0-9]{0,8}$");
    }
    
    public static boolean validarStatus(String texto){
        if(texto == null || texto.isEmpty()) return false;
        
        if(texto.equalsIgnoreCase("cancelado") || texto.equalsIgnoreCase("comprado")) return true;
        return false;
    }
    
    public static boolean validarPositivo(String texto) {
        if(texto == null || texto.isEmpty()) return false;
        
        String patron = "^[0-9]{1,9}$"; // max 9 digitos: evita desbordar Integer.parseInt
        return texto.trim().matches(patron);
    }
    
    public static boolean validarFechaAntes(LocalDate fecha){
        if(fecha == null) return false;
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
        
        String patron = "^(0|[1-9][0-9]{0,7})(\\.[0-9]{1,2})?$"; // permite 0 y montos menores a 1 (0.50)
        return texto.trim().matches(patron);
    }
}
