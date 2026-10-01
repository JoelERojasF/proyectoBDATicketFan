package Negocio;

import dto.BoletoPDFDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.time.format.DateTimeFormatter;

public class GeneradorPDF {

    public static void generar(BoletoPDFDTO boleto, String ruta)
            throws Exception {

        PdfWriter writer = new PdfWriter(ruta);

        PdfDocument pdf = new PdfDocument(writer);

        Document documento = new Document(pdf);

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Paragraph titulo = new Paragraph("TICKETFAN")
                .setBold()
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);

        documento.add(titulo);

        documento.add(
            new Paragraph("BOLETO DE EVENTO")
                .setTextAlignment(TextAlignment.CENTER)
        );

        documento.add(new Paragraph("\n"));

        documento.add(
            new Paragraph("Evento: " + boleto.getNombreEvento())
                .setBold()
        );

        documento.add(
            new Paragraph("Tipo: " + boleto.getTipoEvento())
        );

        documento.add(
            new Paragraph(
                "Edad mínima: " + boleto.getEdadMinima() + " años"
            )
        );

        documento.add(new Paragraph("\n"));

        documento.add(
            new Paragraph(
                "Número de boleto: "
                + boleto.getNumeroBoleto()
            )
        );

        documento.add(
            new Paragraph(
                "Código: "
                + boleto.getCodigoBoleto()
            )
        );

        documento.add(
            new Paragraph(
                String.format(
                    "Precio: $%.2f",
                    boleto.getPrecio()
                )
            )
        );

        documento.add(new Paragraph("\n"));

        documento.add(
            new Paragraph(
                "Cliente: "
                + boleto.getNombreCliente()
            )
        );

        documento.add(
            new Paragraph(
                "Fecha de compra: "
                + boleto.getFechaCompra().format(formato)
            )
        );

        documento.add(new Paragraph("\n"));

        documento.add(
            new Paragraph("BOLETO VÁLIDO")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
        );

        documento.close();
    }
}