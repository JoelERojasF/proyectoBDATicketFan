/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author le0jx
 */
public class CuentaEntidad {
    private int id;
    private double saldo;
    private String banco;
    private String numCuenta;
    private Integer idCliente;
    private Integer idPromotora;

    public CuentaEntidad() {
    }

    public CuentaEntidad(int id, double saldo, String banco, String numCuenta, Integer idCliente, Integer idPromotora) {
        this.id = id;
        this.saldo = saldo;
        this.banco = banco;
        this.numCuenta = numCuenta;
        this.idCliente = idCliente;
        this.idPromotora = idPromotora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdPromotora() {
        return idPromotora;
    }

    public void setIdPromotora(Integer idPromotora) {
        this.idPromotora = idPromotora;
    }

    @Override
    public String toString() {
        return "CuentaEntidad{" + "id=" + id + ", saldo=" + saldo + ", banco=" + banco + ", numCuenta=" + numCuenta + ", idCliente=" + idCliente + ", idPromotora=" + idPromotora + '}';
    }
    
    
}
