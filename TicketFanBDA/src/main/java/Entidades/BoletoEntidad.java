/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author le0jx
 */
public class BoletoEntidad {
    private int id;
    private String numBoleto;
    private String codigoBoleto;
    private double precio;
    private int idEvento;
    private Integer idCompra;

    public BoletoEntidad() {
    }

    public BoletoEntidad(int id, String numBoleto, String codigoBoleto, double precio, int idEvento, Integer idCompra) {
        this.id = id;
        this.numBoleto = numBoleto;
        this.codigoBoleto = codigoBoleto;
        this.precio = precio;
        this.idEvento = idEvento;
        this.idCompra = idCompra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumBoleto() {
        return numBoleto;
    }

    public void setNumBoleto(String numBoleto) {
        this.numBoleto = numBoleto;
    }

    public String getCodigoBoleto() {
        return codigoBoleto;
    }

    public void setCodigoBoleto(String codigoBoleto) {
        this.codigoBoleto = codigoBoleto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    @Override
    public String toString() {
        return "BoletoEntidad{" + "id=" + id + ", numBoleto=" + numBoleto + ", codigoBoleto=" + codigoBoleto + ", precio=" + precio + ", idEvento=" + idEvento + ", idCompra=" + idCompra + '}';
    }
    
    
}
