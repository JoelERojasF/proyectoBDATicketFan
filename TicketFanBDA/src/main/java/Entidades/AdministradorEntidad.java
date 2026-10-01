/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author le0jx
 */
public class AdministradorEntidad {
    private int id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String usuario;
    private String contraseña;
    private Integer idPromotora;

    public AdministradorEntidad() {
    }

    public AdministradorEntidad(int id, String nombres, String apellidoPaterno, String apellidoMaterno, String usuario, String contraseña, Integer idPromotora) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.idPromotora = idPromotora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Integer getIdPromotora() {
        return idPromotora;
    }

    public void setIdPromotora(Integer idPromotora) {
        this.idPromotora = idPromotora;
    }

    @Override
    public String toString() {
        return "AdministradorEntidad{" + "id=" + id + ", nombres=" + nombres + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", usuario=" + usuario + ", contrase\u00f1a=" + contraseña + ", idPromotora=" + idPromotora + '}';
    }
    
    
}
