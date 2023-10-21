package com.tienda.Tools;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tienda.dto.DocumentDTO;
import com.tienda.dto.SaleDetailDTO;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

public class PDFGenerator {
//    public void generatePDF(DocumentDTO document) {
//        try {
//            // Crear una instancia de FileChooser
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
//            // Formatear la fecha en el formato deseado (por ejemplo, "yyyyMMdd_HHmmss")
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmma");
//            String formattedDateTime = dateFormat.format(document.getIssueDate());
//
//            fileChooser.setInitialFileName("TechComputer-"+document.getCustomer().getCustomerFirstName()+" "+formattedDateTime +".pdf"); // Nombre predeterminado del archivo
//
//            // Mostrar el diálogo de selección de archivo
//            File file = fileChooser.showSaveDialog(null);
//
//            if (file != null) {
//                String outputPath = file.getAbsolutePath();
//                String templatePath = "Document.pdf"; // Cambia esto a la ruta de tu plantilla
//
//                PdfReader reader = new PdfReader(templatePath);
//                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
//                AcroFields form = stamper.getAcroFields();
//
//                // Rellenar campos editables con datos de DocumentDTO
//                form.setField("customerName", document.getCustomer().getCustomerFirstName()+" "+document.getCustomer().getCustomerLastName());
//                form.setField("dniOrRuc",document.getCustomer().getCustomerDni());
//                form.setField("date", String.valueOf(document.getIssueDate()));
//                form.setField("documentNumber", document.getDocumentNumber());
//
//
//                // Obtener la lista de detalles de venta (productos)
//                List<SaleDetailDTO> saleDetails = document.getSaleDetails();
//
//                // Iterar sobre los detalles de venta y llenar los campos correspondientes
//                int fieldIndex = 1; // Índice para etiquetar los campos de productos
//                for (SaleDetailDTO saleDetail : saleDetails) {
//                    String fieldNamePrefix = "product" ;
//                    form.setField(fieldNamePrefix +"Quantity"+ fieldIndex  ,String.valueOf(saleDetail.getQuantitySold()));
//                    form.setField(fieldNamePrefix+"Name"+fieldIndex,saleDetail.getProduct().getProductName());
//                    form.setField(fieldNamePrefix+"Price"+fieldIndex , saleDetail.getProduct().getUnitPrice().toString());
//                    form.setField(fieldNamePrefix+"Amount"+fieldIndex , BigDecimal.valueOf(saleDetail.getQuantitySold()).multiply(saleDetail.getProduct().getUnitPrice()).toString());
//                    fieldIndex++;
//                }
//
//                // Rellenar campos con los datos generales de DocumentDTO
//                form.setField("paymentMethod", "Pago en efectivo");
//                form.setField("subTotal", String.valueOf(document.getSubtotal()));
//                form.setField("discountSale", String.valueOf(document.getTotalDiscount()));
//                form.setField("igv", String.valueOf(document.getIgvAmount()));
//                form.setField("totalSale", String.valueOf(document.getTotalAmount()));
//
//
//                // Otras operaciones necesarias
//
//                stamper.setFormFlattening(true);
//                stamper.close();
//
//                // Después de generar el PDF, abrirlo automáticamente
//                downloadPDF(outputPath);
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void generatePDF(DocumentDTO document)  {

        try {
            String outputPath = "TechComputer-" + document.getCustomer().getCustomerFirstName() + ".pdf"; // Cambia esto a la ruta deseada

            String templatePath = "Document.pdf"; // Cambia esto a la ruta de tu plantilla

            PdfReader reader = new PdfReader(templatePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
            AcroFields form = stamper.getAcroFields();

            // Rellenar campos editables con datos de DocumentDTO
            form.setField("customerName", document.getCustomer().getCustomerFirstName() + " " + document.getCustomer().getCustomerLastName());
            form.setField("dniOrRuc", document.getCustomer().getCustomerDni());
            form.setField("date", String.valueOf(document.getIssueDate()));
            form.setField("documentNumber", document.getDocumentNumber());


            // Obtener la lista de detalles de venta (productos)
            List<SaleDetailDTO> saleDetails = document.getSaleDetails();

            // Iterar sobre los detalles de venta y llenar los campos correspondientes
            int fieldIndex = 1; // Índice para etiquetar los campos de productos
            for (SaleDetailDTO saleDetail : saleDetails) {
                String fieldNamePrefix = "product";
                form.setField(fieldNamePrefix + "Quantity" + fieldIndex, String.valueOf(saleDetail.getQuantitySold()));
                form.setField(fieldNamePrefix + "Name" + fieldIndex, saleDetail.getProduct().getProductName());
                form.setField(fieldNamePrefix + "Price" + fieldIndex, saleDetail.getProduct().getUnitPrice().toString());
                form.setField(fieldNamePrefix + "Amount" + fieldIndex, BigDecimal.valueOf(saleDetail.getQuantitySold()).multiply(saleDetail.getProduct().getUnitPrice()).toString());
                fieldIndex++;
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

            // Después de generar el PDF, abrirlo automáticamente
            downloadPDF(outputPath);



        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }


    }


    public String onlyGeneratePDF(DocumentDTO document) {

        String outputPath;
        try {
            outputPath = "TechComputer-" + document.getCustomer().getCustomerFirstName() + ".pdf";

            String templatePath = "Document.pdf"; // Cambia esto a la ruta de tu plantilla

            PdfReader reader = new PdfReader(templatePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
            AcroFields form = stamper.getAcroFields();

            // Rellenar campos editables con datos de DocumentDTO
            form.setField("customerName", document.getCustomer().getCustomerFirstName() + " " + document.getCustomer().getCustomerLastName());
            form.setField("dniOrRuc", document.getCustomer().getCustomerDni());
            form.setField("date", String.valueOf(document.getIssueDate()));
            form.setField("documentNumber", document.getDocumentNumber());


            // Obtener la lista de detalles de venta (productos)
            List<SaleDetailDTO> saleDetails = document.getSaleDetails();

            // Iterar sobre los detalles de venta y llenar los campos correspondientes
            int fieldIndex = 1; // Índice para etiquetar los campos de productos
            for (SaleDetailDTO saleDetail : saleDetails) {
                String fieldNamePrefix = "product";
                form.setField(fieldNamePrefix + "Quantity" + fieldIndex, String.valueOf(saleDetail.getQuantitySold()));
                form.setField(fieldNamePrefix + "Name" + fieldIndex, saleDetail.getProduct().getProductName());
                form.setField(fieldNamePrefix + "Price" + fieldIndex, saleDetail.getProduct().getUnitPrice().toString());
                form.setField(fieldNamePrefix + "Amount" + fieldIndex, BigDecimal.valueOf(saleDetail.getQuantitySold()).multiply(saleDetail.getProduct().getUnitPrice()).toString());
                fieldIndex++;
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

        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        return outputPath;
    }


    public void downloadPDF(String pdfPath) {
        try {
            // Comprobar si Desktop es compatible y el sistema tiene un visor de PDF
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                // Crear un objeto File con la ruta del PDF
                File pdfFile = new File(pdfPath);

                // Abrir el archivo PDF con el visor de PDF predeterminado
                Desktop.getDesktop().open(pdfFile);
            } else {
                // El sistema no admite la apertura de archivos automáticamente
                System.out.println("El sistema no admite la apertura automática de archivos PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void sendPDFByEmail(String recipientEmail, String subject, String body, String pdfPath) {
        // Configuración de las propiedades del servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
        props.put("mail.smtp.port", "587"); // Puerto del servidor SMTP de Gmail
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Habilita STARTTLS

        // Configuración de autenticación (si es necesario)
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("blesgodd34@gmail.com", "zgjs cmax nila tpyp");
            }
        };

        // Crear una sesión de correo
        Session session = Session.getInstance(props, authenticator);

        try {
            // Crea un mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("tu_correo@example.com")); // Tu dirección de correo
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Correo del destinatario
            message.setSubject(subject); // Asunto del correo

            // Crea parte del texto del mensaje
            BodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Crea parte del archivo adjunto (PDF)
            BodyPart pdfPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(pdfPath); // Ruta al archivo PDF
            pdfPart.setDataHandler(new DataHandler(source));
            pdfPart.setFileName("documento.pdf"); // Nombre del archivo adjunto

            // Combina las partes en un correo multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfPart);

            message.setContent(multipart);

            // Enviar el correo
            Transport.send(message);

            System.out.println("Correo enviado satisfactoriamente.");

        } catch (MessagingException e) {
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
