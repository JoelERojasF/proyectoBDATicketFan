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
    private String banco;
    private String numCuenta;
    private Integer idCliente;
    private Integer idPromotora;

    public GuardarCuentaDTO(String banco, String numCuenta, Integer idCliente, Integer idPromotora) {
        this.banco = banco;
        this.numCuenta = numCuenta;
        this.idCliente = idCliente;
        this.idPromotora = idPromotora;
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
    
    
}
