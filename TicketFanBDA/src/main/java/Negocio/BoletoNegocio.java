/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.BoletoEntidad;
import Persistencia.BoletoDAO;
import Persistencia.PersistenciaException;
import dto.BoletoPDFDTO;
import dto.EditarBoletoDTO;
import dto.GuardarBoletoDTO;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author le0jx
 */
public class BoletoNegocio {
    private final BoletoDAO boletoDAO;
    
    private EventoNegocio eventoN;
    private CompraNegocio compraN;

    public BoletoNegocio(BoletoDAO boletoDAO) {
        this.boletoDAO = boletoDAO;
    }

    public void setEventoN(EventoNegocio eventoN) {
        this.eventoN = eventoN;
    }

    public void setCompraN(CompraNegocio compraN) {
        this.compraN = compraN;
    }
    
    public BoletoEntidad guardarBoleto(String numBoleto, String codigoBoleto, String precio, String idEvento) throws NegocioException{
        if(!Validaciones.validarTexto(numBoleto)) throw new NegocioException("El numero del boleto es invalido.");
        if(!Validaciones.validarTexto(codigoBoleto)) throw new NegocioException("El codigo del boleto es invalido.");
        if(!Validaciones.validarCantidadDinero(precio)) throw new NegocioException("EL precio del boleto es invalido");
        if(!Validaciones.validarPositivo(idEvento)) throw new NegocioException("El id buscado del evento es invalido.");
        if(eventoN.BuscarPorID(idEvento) == null) throw new NegocioException("El id buscado del evento no existe.");

        try {
            GuardarBoletoDTO registro = new GuardarBoletoDTO(numBoleto, codigoBoleto, Double.parseDouble(precio), Integer.parseInt(idEvento));
            return boletoDAO.guardarBoleto(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar boleto: " + e.getMessage());
        }
    }
    
    public BoletoEntidad editarBoleto(String id, String numBoleto, String codigoBoleto, String precio, String idEvento, String idCompra) throws NegocioException{
        if(!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        if(!Validaciones.validarTexto(numBoleto)) throw new NegocioException("El numero del boleto es invalido.");
        if(!Validaciones.validarTexto(codigoBoleto)) throw new NegocioException("El codigo del boleto es invalido.");
        if(!Validaciones.validarCantidadDinero(precio)) throw new NegocioException("EL precio del boleto es invalido");
        if(!Validaciones.validarPositivo(idEvento)) throw new NegocioException("El id buscado del evento es invalido.");
        if(eventoN.BuscarPorID(idEvento) == null) throw new NegocioException("El id buscado del evento no existe.");
        if(!Validaciones.validarIdOpcional(idCompra)) throw new NegocioException("El id buscado de la compra es invalido.");
        if(!Validaciones.esVacio(idCompra) && compraN.BuscarPorID(idCompra) == null) throw new NegocioException("El id buscado de la compra no existe.");
        try {
            EditarBoletoDTO registro = new EditarBoletoDTO(Integer.parseInt(id), numBoleto, codigoBoleto, Double.parseDouble(precio), Integer.parseInt(idEvento), Validaciones.esVacio(idCompra) ? null : Integer.valueOf(idCompra.trim()));
            return boletoDAO.editarBoleto(registro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al editar boleto: " + e.getMessage());
        }
    }
    
    public BoletoEntidad eliminarBoleto(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        try {
            return boletoDAO.eliminarBoleto(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar boletos: " + e.getMessage());
        }
    }
    
    public BoletoEntidad BuscarPorID(String id) throws NegocioException{
        if (!Validaciones.validarPositivo(id)) throw new NegocioException("El id buscado del boleto es invalido.");
        try {
            return boletoDAO.BuscarPorID(Integer.parseInt(id));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar boletos: " + e.getMessage());
        }
    }
    
    public List<BoletoEntidad> listarBoletos(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return boletoDAO.listarBoletos(filtro);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar boletos: " + e.getMessage());
        }
    }
    
    /** Boletos de una compra, para que el usuario escoja cual imprimir en PDF. */
    public List<BoletoEntidad> listarBoletosDeCompra(String idCompra) throws NegocioException {
        if (!Validaciones.validarPositivo(idCompra)) throw new NegocioException("El id buscado de la compra es invalido.");
        try {
            return boletoDAO.listarBoletosDeCompra(Integer.parseInt(idCompra.trim()));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al listar los boletos de la compra: " + e.getMessage());
        }
    }
    
    /**
     * Genera el PDF de UN boleto (el que se escoja por su id).
     * @param idBoleto id del boleto a imprimir
     * @param ruta ruta del archivo destino (si no termina en .pdf se agrega)
     * @return la ruta final del PDF generado
     */
    public String generarPDFBoleto(String idBoleto, String ruta) throws NegocioException {
        if (!Validaciones.validarPositivo(idBoleto)) throw new NegocioException("El id buscado del boleto es invalido.");
        if (Validaciones.esVacio(ruta)) throw new NegocioException("La ruta del archivo PDF es invalida.");
        
        String rutaFinal = ruta.trim();
        if (!rutaFinal.toLowerCase().endsWith(".pdf")) {
            rutaFinal += ".pdf";
        }
        try {
            Path carpeta = Paths.get(rutaFinal).toAbsolutePath().getParent();
            if (carpeta != null && !Files.isDirectory(carpeta)) throw new NegocioException("La carpeta de destino no existe.");
        } catch (InvalidPathException e) {
            throw new NegocioException("La ruta del archivo PDF es invalida.");
        }
        
        BoletoPDFDTO datos;
        try {
            datos = boletoDAO.obtenerDatosBoletoPDF(Integer.parseInt(idBoleto.trim()));
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener los datos del boleto: " + e.getMessage());
        }
        if (datos == null) throw new NegocioException("El boleto no existe, no ha sido comprado o su compra fue cancelada.");
        
        try {
            GeneradorPDF.generar(datos, rutaFinal);
            return rutaFinal;
        } catch (Exception e) {
            throw new NegocioException("Error al generar el PDF del boleto: " + e.getMessage());
        }
    }
}
