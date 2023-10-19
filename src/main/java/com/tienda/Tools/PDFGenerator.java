package com.tienda.Tools;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tienda.dto.DocumentDTO;
import com.tienda.dto.ProductDTO;
import com.tienda.dto.SaleDetailDTO;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFGenerator {
    public void generatePDF(DocumentDTO document) {
        try {
            // Crear una instancia de FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("documento.pdf"); // Nombre predeterminado del archivo

            // Mostrar el diálogo de selección de archivo
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                String outputPath = file.getAbsolutePath();
                String templatePath = "Document.pdf"; // Cambia esto a la ruta de tu plantilla

                PdfReader reader = new PdfReader(templatePath);
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
                AcroFields form = stamper.getAcroFields();

                // Rellenar campos editables con datos de DocumentDTO
                form.setField("customerName", document.getCustomer().getCustomerFirstName()+" "+document.getCustomer().getCustomerLastName());
                form.setField("dniOrRuc",document.getCustomer().getCustomerDni());
                form.setField("date", String.valueOf(document.getIssueDate()));
                form.setField("documentNumber", document.getDocumentNumber());


                // Obtener la lista de detalles de venta (productos)
                List<SaleDetailDTO> saleDetails = document.getSale().getSaleDetails();

                // Iterar sobre los detalles de venta y llenar los campos correspondientes
//                int fieldIndex = 1; // Índice para etiquetar los campos de productos
                for (SaleDetailDTO saleDetail : saleDetails) {
                    form.setField("quantity" ,String.valueOf(saleDetail.getQuantitySold()));
                    form.setField("productName",saleDetail.getProduct().getProductName());
                    form.setField("price" , saleDetail.getProduct().getUnitPrice().toString());
                    form.setField("amount" , String.valueOf(saleDetail.getSubtotalPerProduct()));
//                    fieldIndex++;
                }

                // Rellenar campos con los datos generales de DocumentDTO
                form.setField("paymentMethod", "Pago en efectivo");
                form.setField("subTotal", String.valueOf(document.getSubtotal()));
                form.setField("discountSale", String.valueOf(document.getTotalDiscount()));
                form.setField("igv", String.valueOf(document.getIgvAmount()));
                form.setField("totalSale", String.valueOf(document.getTotalAmount()));


                // Otras operaciones necesarias

                stamper.setFormFlattening(true);
                stamper.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillPDF(DocumentDTO document, String templatePath, String outputPath) {
        try {
            PdfReader reader = new PdfReader(templatePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
            AcroFields form = stamper.getAcroFields();

            // Rellenar campos editables con datos de DocumentDTO
            form.setField("documentNumber", document.getDocumentNumber());
            form.setField("issueDate", document.getIssueDate().toString());
            form.setField("subtotal", document.getSubtotal().toString());
            form.setField("igv", document.getIgvAmount().toString());
            form.setField("total", document.getTotalAmount().toString());

            // Otras operaciones necesarias

            stamper.setFormFlattening(true);
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
