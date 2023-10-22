package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.User.UserSession;
import com.tienda.Dao.CustomerDAO;
import com.tienda.Dao.DocumentDAO;
import com.tienda.Dao.ProductDAO;
import com.tienda.Dao.SaleDAO;
import com.tienda.DaoImpl.CustomerDAOHibernate;
import com.tienda.DaoImpl.DocumentDAOHibernate;
import com.tienda.DaoImpl.ProductDAOHibernate;
import com.tienda.DaoImpl.SaleDAOHibernate;
import com.tienda.Tools.PDFGenerator;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.Utils.CustomerUtil;
import com.tienda.Utils.DocumentUtil;
import com.tienda.Utils.ProductUtil;
import com.tienda.dto.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ModalSale extends StackPane implements Initializable {

    @FXML
    private MFXButton addProductSaleButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXTextField txtValidateCustomer;


    @FXML
    private MFXLegacyComboBox<String> cbProducts;

    @FXML
    private MFXButton cleanButton;

    @FXML
    private TableColumn<SaleDetailDTO, Void> colActions;

    @FXML
    private TableColumn<SaleDetailDTO, String> colBrand;

    @FXML
    private TableColumn<SaleDetailDTO, Long> colCode;


    @FXML
    private TableColumn<SaleDetailDTO, String> colDescription;

    @FXML
    private TableColumn<SaleDetailDTO, BigDecimal> colPrice;

    @FXML
    private TableColumn<SaleDetailDTO, String> colProductName;

    @FXML
    private TableColumn<SaleDetailDTO, String> colQuantity;

    @FXML
    private TableColumn<SaleDetailDTO, String> colSubtotal;

    @FXML
    private TableColumn<SaleDetailDTO, String> colTotal;

    @FXML
    private TableColumn<SaleDetailDTO, String> colDiscount;



    @FXML
    private MFXButton confirmSaleButton;

    @FXML
    private MFXLegacyTableView<SaleDetailDTO> saleTable;

    @FXML
    private StackPane stackPane;

    @FXML
    private MFXTextField txtProductPrice;

    @FXML
    private MFXTextField txtAvailableStock;

    @FXML
    private MFXTextField txtDiscount;

    @FXML
    private MFXTextField txtQuantity;

    @FXML
    private MFXTextField txtSaleNumber;

    @FXML
    public MFXTextField txtSearchCustomer;

    @FXML
    private MFXTextField txtSearchProduct;

    @FXML
    private MFXTextField txtTotalDiscount;

    @FXML
    private MFXTextField txtTotalSale;

    @FXML
    private MFXButton newCustomer;

    @FXML
    private Label labelValidateCustomer;

    @FXML
    private Label labelValidateDiscount;

    @FXML
    private Label labelValidateQuantity;


    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado

    SaleDAO saleDAO;

    CustomerDAO customerDAO;
    DocumentDAO documentDAO;
    TextFieldValidator quantityValidator;
    TextFieldValidator discountValidator;

    TextFieldValidator customerValidator;

    ProductDTO selectedProduct;

    //id del cliente
    long customerId;



    // Calcula el descuento total
    BigDecimal totalDiscount = BigDecimal.ZERO;

    // Calcula la suma del subTotal
    BigDecimal totalSumSubTotal = BigDecimal.ZERO; // Inicializa con 0.00

    // Calcula la suma total
    BigDecimal totalSumSale= BigDecimal.ZERO; // Inicializa con 0.00

    ProductDAO productDAO;

    CustomerUtil customerUtil;

    DocumentUtil documentUtil;

   long saleId=0;


    public ModalSale() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalSale.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setAddProductToTableButtonAction(EventHandler<ActionEvent> action) {
        addProductSaleButton.setOnAction(action);
    }

    public void setCleanButtonAction(EventHandler<ActionEvent> action) {
        cleanButton.setOnAction(action);
    }

    public void setConfirmSaleButtonAction(EventHandler<ActionEvent> action) {
        confirmSaleButton.setOnAction(action);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> action) {
        cancelButton.setOnAction(action);
    }

    public void setNewCustomer(EventHandler<ActionEvent> action){
        newCustomer.setOnAction(action);
    }

    public void configureModal(
            EventHandler<ActionEvent> newCustomer,EventHandler<ActionEvent> addProductToTable, EventHandler<ActionEvent> cleanItems, EventHandler<ActionEvent> confirmSale, EventHandler<ActionEvent> cancelSale) {
        setNewCustomer(newCustomer);
        setAddProductToTableButtonAction(addProductToTable);
        setCleanButtonAction(cleanItems);
        setConfirmSaleButtonAction(confirmSale);
        setCancelButtonAction(cancelSale);
    }





    public void cleanInputs() {
        txtSearchCustomer.setText("");
        txtSearchCustomer.setText("");
        txtSearchProduct.setText("");
        txtQuantity.setText("");
        txtSearchCustomer.setText("");
        txtAvailableStock.setText("");
        txtDiscount.setText("");

        txtValidateCustomer.setText("");
        cbProducts.setValue(null);
        txtTotalSale.setText("");
        txtProductPrice.setText("");
        txtTotalDiscount.setText("");



    }


    public void handleNewCustomer(StackPane root){
        //Crear una instancia del modal
        ModalCustomer modalCustomer = new ModalCustomer();
        // Configurar el modal mediante un solo método
        modalCustomer.configureModal("Nuevo cliente",
                "Completa los campos para registrar un nuevo cliente", "Registrar",
                eventCustomer1-> {

                    List<String> errorMessages = new ArrayList<>();
                    boolean validationSuccessful = modalCustomer.validateTextFieldsCustomer(errorMessages);

                    if (validationSuccessful) {
                        // Obtener el RUC y el correo electrónico ingresados en el formulario
                        String dni = modalCustomer.txtCustomerDni.getText().trim();
                        String ruc = modalCustomer.txtCustomerRuc.getText().trim();
                        String email = modalCustomer.txtCustomerEmail.getText().trim();


                        // Verificar si ya existe un cliente con el mismo correo electrónico
                        if (customerDAO.isDniExists(dni)) {
                            // Mostrar un mensaje de error si el cliente ya existe con el mismo correo electrónico
                            errorMessages.add("Un cliente ya existe con este DNI.");
                        }

                        // Verificar si ya existe un cliente con el mismo RUC
                        if (customerDAO.isRucExists(ruc)) {
                            // Mostrar un mensaje de error si el cliente ya existe con el mismo RUC
                            errorMessages.add("Un cliente ya existe con este RUC.");
                        }

                        // Verificar si ya existe un cliente con el mismo correo electrónico
                        if (customerDAO.isEmailExists(email)) {
                            // Mostrar un mensaje de error si el cliente ya existe con el mismo correo electrónico
                            errorMessages.add("Un cliente ya existe con este correo electrónico.");
                        }

                        // Si no hay errores, proceder a guardar el cliente
                        if (errorMessages.isEmpty()) {

                            // La validación fue exitosa, procede a crear el cliente
                            CustomerDTO newCustomerDTO = new CustomerDTO();
                            newCustomerDTO.setCustomerDni(modalCustomer.txtCustomerDni.getText().trim());
                            newCustomerDTO.setCustomerRuc(modalCustomer.txtCustomerRuc.getText().trim());
                            newCustomerDTO.setCustomerLastName(modalCustomer.txtCustomerLastNames.getText().trim());
                            newCustomerDTO.setCustomerFirstName(modalCustomer.txtCustomerNames.getText().trim());
                            newCustomerDTO.setCustomerAddress(modalCustomer.txtCustomerAdress.getText().trim());
                            newCustomerDTO.setCustomerEmail(modalCustomer.txtCustomerEmail.getText().trim());
                            newCustomerDTO.setCustomerPhoneNumber(modalCustomer.txtCustomerPhoneNumber.getText().trim());
                            // Intenta registrar al cliente
                            customerDAO.saveCustomer(newCustomerDTO);

                            // Muestra un mensaje de éxito si es necesario
                            //Crear una instancia del modal
                            ModalDialog modalDialog = new ModalDialog();
                            // Configurar el modal mediante un solo método
                            modalDialog.configureModal(
                                    new Image("Images/iconCheck.png"),
                                    "Registrado correctamente.",
                                    "Cliente registrado correctamente.",
                                    "OK",
                                    ev -> {
                                        modalDialog.close();
                                        modalCustomer.close();


                                        if(modalCustomer.txtCustomerDni.getText().trim().isEmpty()){
                                            txtSearchCustomer.setText(ruc);
                                        }

                                        if(modalCustomer.txtCustomerRuc.getText().trim().isEmpty()){
                                            txtSearchCustomer.setText(dni);
                                        }
                                        if(!modalCustomer.txtCustomerDni.getText().trim().isEmpty() && !modalCustomer.txtCustomerRuc.getText().trim().isEmpty() ){
                                            txtSearchCustomer.setText(ruc);
                                        }

                                    }
                            );
                            modalDialog.showModal(root);

                        } else {
                            // Mostrar mensajes de error si existen problemas de validación
                            StringBuilder errorMessageBuilder = new StringBuilder();
                            for (String errorMessage : errorMessages) {
                                System.out.println(errorMessage);
                                errorMessageBuilder.append("- ").append(errorMessage).append("\n");
                            }

                            String errorMessageText = errorMessageBuilder.toString();

                            ModalDialog modalDialog = new ModalDialog();
                            modalDialog.configureModal(
                                    new Image("Images/alert.png"),
                                    "Registro incorrecto.",
                                    "No se pudo registrar el cliente correctamente: \n" + "\n" + errorMessageText,
                                    "OK",
                                    ev -> {
                                        System.out.println("Se hizo clic en Confirmar");
                                        modalDialog.close();
                                    }
                            );

                            modalDialog.showModal(root);
                        }

                    } else {
                        // La validación falló, muestra los mensajes de error
                        // Construir el mensaje de error
                        StringBuilder errorMessageBuilder = new StringBuilder();
                        for (String errorMessage : errorMessages) {
                            System.out.println(errorMessage);
                            errorMessageBuilder.append("- ").append(errorMessage).append("\n");
                        }

                        String errorMessageText = errorMessageBuilder.toString();

                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(
                                new Image("Images/alert.png"),
                                "Registro incorrecto.",
                                "No se pudo registrar el cliente correctamente: \n" + "\n" + errorMessageText,
                                "OK",
                                ev -> {
                                    // Lógica cuando se hace clic en el botón "Confirmar"
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();
                                }
                        );

                        modalDialog.showModal(root);
                        // También puedes mostrar un mensaje de error general si lo deseas
                        System.out.println("No se pudo crear el cliente debido a errores de validación.");
                    }

                },
                eventCustomer2 -> {
                    modalCustomer.close(); // Cierra el modal


                }


        );

        modalCustomer.showModal(root);


    }



    public void addProductDetailToTable() {
        // Validación de los datos aquí
        List<String> errorMessages = new ArrayList<>();
        boolean validationSuccessful = validateTextFieldsSale(errorMessages);

        if (validationSuccessful) {
            long code = selectedProduct.getProductId();
            String productName = selectedProduct.getProductName();
            String brand = selectedProduct.getProductBrand();
            String description = selectedProduct.getProductDescription();
            BigDecimal price = selectedProduct.getUnitPrice();
            int quantity = Integer.parseInt(txtQuantity.getText());
            BigDecimal discount = new BigDecimal(txtDiscount.getText()); // Convierte el descuento a BigDecimal


            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(quantity)); // Calcula el subtotal
            BigDecimal total = subTotal.subtract(discount); // Calcula el total



            // Crea un nuevo PurchaseDetailDTO
            SaleDetailDTO saleDetailDTO= new SaleDetailDTO();
            ProductDTO productDTO= new ProductDTO();
            productDTO.setProductId(code);
            productDTO.setProductName(productName);
            productDTO.setProductBrand(brand);
            productDTO.setProductDescription(description);
            productDTO.setUnitPrice(price);
            saleDetailDTO.setProduct(productDTO);
            saleDetailDTO.setQuantitySold(quantity);
            saleDetailDTO.setDiscountPerProduct(discount);
            saleDetailDTO.setSubtotalPerProduct(subTotal);
            saleDetailDTO.setTotal(total);

            // Agrega el purchaseDetail a la lista observable de datos
            saleTable.getItems().add(saleDetailDTO);

            // Limpia los campos de texto después de agregar los datos
            txtDiscount.clear();
            txtQuantity.clear();

            // Después de agregar el nuevo elemento, acumula los descuentos nuevamente desde cero.
            totalDiscount = BigDecimal.ZERO;

            // Calcula el descuento total en el mismo método
            for (SaleDetailDTO item : saleTable.getItems()) {
                totalDiscount=totalDiscount.add(item.getDiscountPerProduct());
            }
            txtTotalDiscount.setText(String.valueOf(totalDiscount));

            // Después de agregar el nuevo elemento, acumula los descuentos nuevamente desde cero.
            totalSumSubTotal = BigDecimal.ZERO;

            // Calcula la suma del subtotal en el mismo método
            for (SaleDetailDTO item : saleTable.getItems()) {
                totalSumSubTotal =  totalSumSubTotal.add(item.getSubtotalPerProduct());
            }
            txtTotalSale.setText(String.valueOf(totalSumSubTotal));


            // Después de agregar el nuevo elemento, acumula los descuentos nuevamente desde cero.
            totalSumSale = BigDecimal.ZERO;

            // Calcula la suma total en el mismo método
            for (SaleDetailDTO item : saleTable.getItems()) {
                totalSumSale=  totalSumSale.add(item.getTotal());
            }
            txtTotalSale.setText(String.valueOf(totalSumSale));



        } else {

            // Mostrar mensajes de error si existen problemas de validación
            StringBuilder errorMessageBuilder = new StringBuilder();
            for (String errorMessage : errorMessages) {
                System.out.println(errorMessage);
                errorMessageBuilder.append("- ").append(errorMessage).append("\n");
            }

            String errorMessageText = errorMessageBuilder.toString();

            ModalDialog modalDialog = new ModalDialog();
            modalDialog.configureModal(
                    new Image("Images/alert.png"),
                    "Problema al áñádir productos",
                    "No se pudo añadir el producto a la venta: \n" + "\n" + errorMessageText,
                    "OK",
                    ev -> {
                        System.out.println("Se hizo clic en Confirmar");
                        modalDialog.close();
                    }
            );

            modalDialog.showModal(stackPane);


        }
    }



    public void confirmSale() {


        if (txtTotalSale.getText().equals("0")|| txtTotalSale.getText().isEmpty()) {
            ModalDialog modalDialog = new ModalDialog();
            modalDialog.configureModal(
                    new Image("Images/alert.png"),
                    "Error al realizar la venta",
                    "Agrega productos a la venta: \n",
                    "OK",
                    ev -> {
                        modalDialog.close();
                    }
            );
            modalDialog.showModal(stackPane);

        } else {


            long userId = 0;
            UserSession session = UserSession.getInstance();
            if (session.isLoggedIn()) {
                //Obtener la el id del usuario que ha iniciado sesson en el sistema.
                userId = session.getUserId();

            }
            UserDTO userDetail = new UserDTO();
            userDetail.setUserId(userId);

            CustomerDTO customerDetail = new CustomerDTO();
            customerDetail.setCustomerId(customerId);

            // Obtener la fecha y hora actual como un objeto java.util.Date
            Date currentDate = new Date();


            SaleDTO sale= new SaleDTO();
            sale.setSaleDateTime(currentDate);
            sale.setSubtotal(totalSumSubTotal);
            sale.setDiscountTotal(totalDiscount);
            sale.setTotal(totalSumSale);
            sale.setCustomer(customerDetail);
            sale.setUser(userDetail);

            // Crear una lista para almacenar los detalles de compra
            List<SaleDetailDTO> saleDetails = new ArrayList<>();

            // Recorrer las filas de la tabla y obtener los datos
            for (SaleDetailDTO row : saleTable.getItems()) {
                // Crear un nuevo objeto PurchaseDetailDTO en cada iteración
                SaleDetailDTO saleDetail = new SaleDetailDTO();
                // Obtener los datos de la fila y establecerlos en el detalle de compra
                saleDetail.setQuantitySold(row.getQuantitySold());
                saleDetail.setDiscountPerProduct(row.getDiscountPerProduct());
                //total de la tabla
                saleDetail.setSubtotalPerProduct(row.getTotal());

                ProductDTO productDetail= new ProductDTO();
                productDetail.setProductId(row.getProduct().getProductId());
                saleDetail.setProduct(productDetail);

                // Agregar el detalle de compra a la lista
                saleDetails.add(saleDetail);


            }

            // Ahora tienes una lista de detalles de compra que puedes asignar a tu objeto PurchaseDTO
            sale.setSaleDetails(saleDetails);

            // Guardar la compra en la base de datos
            saleDAO.saveSale(sale);

            // Después de guardar la compra, actualizar el stock de los productos
            for (SaleDetailDTO detail : saleDetails) {
                productDAO.updateProductStockOnSale(detail.getProduct().getProductId(), detail.getQuantitySold());
            }

            //Limpiar la tabla
            saleTable.getItems().clear();

            //Limpiar todos los campos
            cleanInputs();



            // Crear un nuevo objeto DocumentDTO con la información del documento
            DocumentDTO newDocument = new DocumentDTO();
            DocumentTypeDTO documentTypeDTO= new DocumentTypeDTO();

            documentTypeDTO.setDocumentTypeId(1L);
            newDocument.setDocumentType(documentTypeDTO);// Establece el tipo de documento
            newDocument.setDocumentNumber(String.valueOf(saleId)); // Establece el número de documento
            newDocument.setIssueDate(currentDate); // Establece la fecha de emisión
            SaleDTO saleDTO= new SaleDTO();
            saleDTO.setSaleId(saleId);
            newDocument.setSale(saleDTO); // Establece la venta relacionada (si es relevante)
            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerId(customerId);
            newDocument.setCustomer(customer);// Establece el cliente relacionado (si es relevante)
            newDocument.setUser(userDetail); // Establece el usuario relacionado (si es relevante)
            newDocument.setSubtotal(totalSumSubTotal); // Establece el subtotal


            BigDecimal igvAmount= new BigDecimal("0.18").multiply(totalSumSubTotal);
            BigDecimal totalAmount= new BigDecimal(String.valueOf(totalSumSale.add(igvAmount)));

            newDocument.setTotalDiscount(totalDiscount);
            newDocument.setIgvAmount(igvAmount); // Establece el IGV
            newDocument.setTotalAmount(totalAmount); // Establece el total
            documentUtil.saveDocument(newDocument);






            //Crear una instancia del modal
            ModalDialog modalSendEmail = new ModalDialog();
            // Configurar el modal mediante un solo método
            modalSendEmail.configureModal(
                    new Image("Images/logoEmail.png"),
                    "Venta exitosa",
                    " Enviar recibo de venta al cliente: ",
                    "Enviar",
                    "Cancelar",
                    e -> {
                        DocumentDTO documentSendEmail= documentDAO.getDocumentBySaleId(saleDAO.getLastSaleId());
                        String recipientEmail = documentSendEmail.getCustomer().getCustomerEmail();
                        System.out.println(recipientEmail);

                        String names= documentSendEmail.getCustomer().getCustomerFirstName();
                        String lastNames= documentSendEmail.getCustomer().getCustomerLastName();
                        String namesCustomer= names.concat(" "+lastNames);


                        String subject = "¡Su Comprobante Electrónico Está Listo!";
                        String body = "Estimado " + namesCustomer + ",\n\n" +
                                "Es un placer informarle que su comprobante electrónico ya está listo para su revisión. " +
                                "Puede acceder a su comprobante electrónico en su cuenta.\n\n" +
                                "Por favor, no dude en ponerse en contacto si necesita alguna asistencia adicional o tiene alguna pregunta.\n\n" +
                                "Gracias por su confianza en nuestros servicios.\n\n" +
                                "Atentamente,\n" +
                                "TechComputer";



                        PDFGenerator pdfGenerator = new PDFGenerator();
                        String pdfPath =pdfGenerator.onlyGeneratePDF(documentSendEmail);
                        PDFGenerator.sendPDFByEmail(recipientEmail, subject, body, pdfPath);

                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(new Image("Images/iconCheck.png"),
                                "Comprobante enviado.",
                                "Comprobante exitoso enviado correctamente.",
                                "Ok",
                                ev -> {
                                    modalSendEmail.close();
                                    modalDialog.close(); // Cierra el modal
                                });

                        modalDialog.showModal(stackPane);


                    },
                    e -> {
                        // Lógica cuando se hace clic en el botón "Cancelar"
                        modalSendEmail.close(); // Cierra el modal
                    }
            );

            modalSendEmail.showModal(stackPane);

            saleId= saleId+1;

            txtSaleNumber.setText(String.valueOf(saleId));
        }

    }





    public boolean validateTextFieldsSale(List<String> errorMessages) {
        boolean validationSuccessful = true;
        String discountText = txtDiscount.getText().trim(); // Obtener el texto del campo DNI
        String quantityText = txtQuantity.getText().trim(); // Obtener el texto del campo RUC
        String customerText = txtValidateCustomer.getText().trim(); // Obtener el texto del campo RUC

        if (txtDiscount.getText().isEmpty() || txtQuantity.getText().isEmpty() || cbProducts.getValue() == null ) {
            errorMessages.add("Completa todos los campos.\n");
            validationSuccessful = false;
        }

        if (validationSuccessful) {

            if (!discountValidator.validateDiscount(discountText)) {
                errorMessages.add("El descuento no cumple con los requisitos.\n");
                validationSuccessful = false;
            }
            if (!quantityValidator.validateQuantity(quantityText)) {
                errorMessages.add("La cantidad no cumple con los requisitos.\n");
                validationSuccessful = false;
            }
            if(!customerValidator.validateCustomerExists(customerText)){
                errorMessages.add("El cliente no existe.\n");
                validationSuccessful = false;
            }

        }

        return validationSuccessful;
    }


    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        saleTable.setOnMouseExited(event -> {
            // Deseleccionar la fila activa
            saleTable.getSelectionModel().clearSelection();
        });

        // Configura las columnas de la tabla una vez en initialize()

        // Configura la columna "colCode" para mostrar el ID del producto.
        colCode.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductId()));
        // Configura la columna "colProductName" para mostrar el nombre del producto.
        colProductName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductName()));
        // Configura la columna "colBrand" para mostrar la marca del producto.
        colBrand.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductBrand()));
        // Configura la columna "colDescription" para mostrar la descripción del producto.
        colDescription.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductDescription()));
        // Configura la columna "colPrice" para mostrar el precio del producto.
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getUnitPrice()));

        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountPerProduct"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotalPerProduct"));
        //Nuevo campo agregado solamente para mostrar el total de venta de un producto
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        // Configura la columna de acciones (botón eliminar)
        colActions.setCellFactory(param -> new TableCell<>() {
            final MFXButton deleteButton = new MFXButton("Eliminar");

            {
                deleteButton.setOnAction(event -> {
                    SaleDetailDTO selectedItem = getTableView().getItems().get(getIndex());
                    saleTable.getItems().remove(selectedItem);
                    saleTable.refresh();

                    if (selectedItem != null) {
                        saleTable.getItems().remove(selectedItem);
                        // Recalcula la suma total después de eliminar

                        totalSumSale = BigDecimal.ZERO;
                        for ( SaleDetailDTO saleDetail : saleTable.getItems()) {
                            totalSumSale = totalSumSale.add(saleDetail.getTotal());
                        }
                        txtTotalSale.setText(String.valueOf(totalSumSale));

                        totalDiscount = BigDecimal.ZERO;
                        for (SaleDetailDTO item : saleTable.getItems()) {
                            totalDiscount = totalDiscount.add(item.getDiscountPerProduct());
                        }
                        txtTotalDiscount.setText(String.valueOf(totalDiscount));

                    }


                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });


    }


















    public void showModal(StackPane parentStackPane) {
        // Añade el fondo oscuro al StackPane
        backdrop = createBackdrop(parentStackPane);
        parentStackPane.getChildren().add(backdrop);
        // Agrega un evento para cerrar el modal haciendo clic fuera de él
        backdrop.setOnMouseClicked(event -> close());


        // Calcula la posición para que el modal aparezca en el centro del StackPane
        double centerX = parentStackPane.getWidth() / 2;
        double centerY = parentStackPane.getHeight() / 2;

        // Establece la posición inicial del modal
        setLayoutX(centerX - (getPrefWidth() / 2));
        setLayoutY(centerY - (getPrefHeight() / 2));


        // Añade el modal al StackPane
        parentStackPane.getChildren().add(this);

        // Aplica la animación de zoom y opacidad con interpoladores personalizados
        setScaleX(0.7);
        setScaleY(0.7);
        setOpacity(0);

        // Interpolador personalizado para la escala
        Interpolator scaleInterpolator = Interpolator.SPLINE(0.4, 0.1, 0.2, 1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(scaleInterpolator);

        // Interpolador personalizado para la opacidad
        Interpolator opacityInterpolator = Interpolator.SPLINE(0.1, 0.1, 0.25, 1);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(1);
        fadeTransition.setInterpolator(opacityInterpolator);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();


    }

    private StackPane createBackdrop(StackPane parentStackPane) {
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Color de fondo oscuro
        backdrop.setPrefSize(parentStackPane.getWidth(), parentStackPane.getHeight());
        return backdrop;
    }

    public void close() {
        if (isClosed) {
            return; // Evitar cierre múltiple
        }

        isClosed = true; // Establecer la bandera de cierre

        // Aplica la animación inversa al cerrar el modal
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(0.1);
        scaleTransition.setToY(0.1);
        scaleTransition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(0);
        fadeTransition.setInterpolator(Interpolator.SPLINE(0.1, 0.1, 0.25, 1));

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> {
            StackPane parentStackPane = (StackPane) getParent();
            parentStackPane.getChildren().remove(backdrop);
            parentStackPane.getChildren().remove(this); // Remueve el modal
        });
        parallelTransition.play();
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        saleDAO= new SaleDAOHibernate(HibernateUtil.getSessionFactory());
        customerDAO= new CustomerDAOHibernate(HibernateUtil.getSessionFactory());
        productDAO= new ProductDAOHibernate(HibernateUtil.getSessionFactory());
        documentDAO= new DocumentDAOHibernate(HibernateUtil.getSessionFactory());

        documentUtil= new DocumentUtil(documentDAO);


        saleId= saleDAO.getLastSaleId()+1;

        txtSaleNumber.setText(String.valueOf(saleId));





        discountValidator= new TextFieldValidator(txtDiscount, labelValidateDiscount, TextFieldValidator.ValidationCriteria.DISCOUNT);
        quantityValidator = new TextFieldValidator(txtQuantity, labelValidateQuantity, TextFieldValidator.ValidationCriteria.QUANTITY);
        customerValidator = new TextFieldValidator(txtValidateCustomer, labelValidateCustomer, TextFieldValidator.ValidationCriteria.CUSTOMER_EXISTS);



        ProductUtil productUtil = new ProductUtil(txtSearchProduct,cbProducts);
        productUtil.startProductSearchAndSetProductName();
        productUtil.loadProductNamesToComboBox();


        customerUtil= new CustomerUtil(txtSearchCustomer,txtValidateCustomer);
//        customerUtil.startCustomerSearchCustomer();

        txtSearchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.trim();

            customerUtil.handleSearchCustomerAsync(value).thenAcceptAsync(foundCustomer -> {
                if (foundCustomer != null) {
                    // Aquí puedes usar 'foundCustomer' donde lo necesites
                    System.out.println("Cliente encontrado: " + foundCustomer.getCustomerId());
                    customerId=foundCustomer.getCustomerId();
                } else {
                    System.out.println("Cliente no encontrado.");
                }
            });
        });





        cbProducts.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // El nuevo valor seleccionado (nombre del producto) está en newValue
                String selectedProductName = newValue;

                // Obtén los datos completos del producto seleccionado y guardarlo en el DTO
                selectedProduct = productUtil.getProductByName(selectedProductName);

                if (selectedProduct != null) {
                    // Haz lo que necesites con los datos del producto seleccionado
//                    System.out.println("Producto seleccionado: " + selectedProductName);
//                    System.out.println("ID del producto: " + selectedProduct.getProductId());
//                    System.out.println("Descripción: " + selectedProduct.getProductDescription());

                    txtAvailableStock.setText(String.valueOf(selectedProduct.getAvailableStock()));
                    txtProductPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));

                    // ... y otros campos del producto
                } else {
                    // Maneja el caso en el que no se encuentra el producto
                    System.out.println("Producto no encontrado.");
                }
            }
        });


    }
}
