package Negocio;

import dto.BoletoPDFDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeneradorPDF {

    /** Genera un PDF con los datos de UN boleto en la ruta indicada. */
    public static void generar(BoletoPDFDTO boleto, String ruta) throws Exception {
        PdfWriter writer = new PdfWriter(ruta);
        PdfDocument pdf = new PdfDocument(writer);
        Document documento = new Document(pdf);
        try {
            escribirContenido(documento, boleto);
        } finally {
            documento.close();   // se cierra siempre, aunque falle algo al escribir
        }
    }

    private static void escribirContenido(Document documento, BoletoPDFDTO boleto) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        documento.add(new Paragraph("TICKETFAN")
                .setBold()
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER));

        documento.add(new Paragraph("BOLETO DE EVENTO")
                .setTextAlignment(TextAlignment.CENTER));

        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Evento: " + boleto.getNombreEvento()).setBold());
        documento.add(new Paragraph("Tipo: " + boleto.getTipoEvento()));
        documento.add(new Paragraph(boleto.getEdadMinima() > 0
                ? "Edad mínima: " + boleto.getEdadMinima() + " años"
                : "Edad mínima: sin restricción"));

        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Número de boleto: " + boleto.getNumeroBoleto()));
        documento.add(new Paragraph("Código: " + boleto.getCodigoBoleto()));
        documento.add(new Paragraph(String.format(Locale.US, "Precio: $%.2f", boleto.getPrecio())));

        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Cliente: " + boleto.getNombreCliente()));
        documento.add(new Paragraph("Fecha de compra: " + boleto.getFechaCompra().format(formato)));

        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("BOLETO VÁLIDO")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));
    }
}
