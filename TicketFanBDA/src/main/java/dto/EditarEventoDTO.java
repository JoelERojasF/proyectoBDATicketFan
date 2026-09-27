/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author le0jx
 */
public class EditarEventoDTO {
    private int id;
    private String nombreShow;
    private String tipo;
    private int edadMinima;
    private String imagenPromocional;
    private int cantidadBoletos;
    private int idAdministrador;
    private int idCuenta;

    public EditarEventoDTO(int id, String nombreShow, String tipo, int edadMinima, String imagenPromocional, int cantidadBoletos, int idAdministrador, int idCuenta) {
        this.id = id;
        this.nombreShow = nombreShow;
        this.tipo = tipo;
        this.edadMinima = edadMinima;
        this.imagenPromocional = imagenPromocional;
        this.cantidadBoletos = cantidadBoletos;
        this.idAdministrador = idAdministrador;
        this.idCuenta = idCuenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreShow() {
        return nombreShow;
    }

    public void setNombreShow(String nombreShow) {
        this.nombreShow = nombreShow;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public String getImagenPromocional() {
        return imagenPromocional;
    }

    public void setImagenPromocional(String imagenPromocional) {
        this.imagenPromocional = imagenPromocional;
    }

    public int getCantidadBoletos() {
        return cantidadBoletos;
    }

    public void setCantidadBoletos(int cantidadBoletos) {
        this.cantidadBoletos = cantidadBoletos;
    }

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    
    
}
