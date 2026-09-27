/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author le0jx
 */
public class GuardarCuentaDTO {
    private double saldo;
    private String banco;
    private String numCuenta;
    private int idCliente;
    private int idPromotora;

    public GuardarCuentaDTO(double saldo, String banco, String numCuenta, int idCliente, int idPromotora) {
        this.saldo = saldo;
        this.banco = banco;
        this.numCuenta = numCuenta;
        this.idCliente = idCliente;
        this.idPromotora = idPromotora;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdPromotora() {
        return idPromotora;
    }

    public void setIdPromotora(int idPromotora) {
        this.idPromotora = idPromotora;
    }
    
    
}
