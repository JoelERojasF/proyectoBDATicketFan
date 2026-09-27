/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;

/**
 *
 * @author le0jx
 */
public class GuardarCompraDTO {
    private int id;
    private String detalles;
    private Double total;
    private LocalDateTime fechaHora;
    private int idCliente;
    private int idCuenta;

    public GuardarCompraDTO(int id, String detalles, Double total, LocalDateTime fechaHora, int idCliente, int idCuenta) {
        this.id = id;
        this.detalles = detalles;
        this.total = total;
        this.fechaHora = fechaHora;
        this.idCliente = idCliente;
        this.idCuenta = idCuenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    
    
}
