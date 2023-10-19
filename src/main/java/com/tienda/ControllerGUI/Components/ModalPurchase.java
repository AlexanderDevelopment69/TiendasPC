package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.User.UserSession;
import com.tienda.Dao.ProductDAO;
import com.tienda.Dao.PurchaseDAO;
import com.tienda.Dao.PurchaseDetailDAO;
import com.tienda.Dao.SupplierDAO;
import com.tienda.DaoImpl.ProductDAOHibernate;
import com.tienda.DaoImpl.PurchaseDAOHibernate;
import com.tienda.DaoImpl.PurchaseDetailDAOHibernate;
import com.tienda.DaoImpl.SupplierDAOHibernate;
import com.tienda.Model.Supplier;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.Utils.ProductUtil;
import com.tienda.Utils.SupplierUtil;
import com.tienda.dto.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ModalPurchase extends StackPane implements Initializable {
    @FXML
    private MFXButton addProductPurchaseButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXLegacyComboBox<String> cbSupplierProducts;
    @FXML
    private MFXLegacyComboBox<String> cbSupplierName;

    @FXML
    private MFXButton cleanButton;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colProductName;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colBrand;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colCode;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colDescription;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colCost;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colQuantity;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colSubTotal;

    @FXML
    private TableColumn<PurchaseDetailDTO, Void> colActions;
    @FXML
    private MFXButton confirmPurchaseButton;

    @FXML
    private MFXLegacyTableView<PurchaseDetailDTO> purchaseTable;

    @FXML
    private StackPane stackPane;

    @FXML
    public MFXTextField txtCost;

    @FXML
    public MFXTextField txtPurchaseNumber;

    @FXML
    public MFXTextField txtQuantity;

    @FXML
    public MFXTextField txtSupplierName;

    @FXML
    public MFXTextField txtTotalPurchase;

    @FXML
    public MFXTextField txtSearchProduct;

    @FXML
    public MFXTextField txtSearchSupplier;

    @FXML
    private Label labelValidateCost;

    @FXML
    private Label labelValidateQuantity;


    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado

    SupplierDAO supplierDAO;

    PurchaseDAO purchaseDAO;

    ProductDAO productDAO;
    PurchaseDetailDAO purchaseDetailDAO;


    ProductDTO selectedProduct = new ProductDTO();


    Long selectedSupplierId;


    TextFieldValidator costValidator;
    TextFieldValidator quantityValidator;




    public ModalPurchase() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalPurchase.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public void setAddProductToTableButtonAction(EventHandler<ActionEvent> action) {
        addProductPurchaseButton.setOnAction(action);
    }

    public void setCleanButtonAction(EventHandler<ActionEvent> action) {
        cleanButton.setOnAction(action);
    }

    public void setConfirmPurchaseButtonAction(EventHandler<ActionEvent> action) {
        confirmPurchaseButton.setOnAction(action);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> action) {
        cancelButton.setOnAction(action);
    }


    public void configureModal(
            EventHandler<ActionEvent> addProductToTable, EventHandler<ActionEvent> cleanItems, EventHandler<ActionEvent> confirmPurchase, EventHandler<ActionEvent> cancelPurchase) {

        setAddProductToTableButtonAction(addProductToTable);
        setCleanButtonAction(cleanItems);
        setConfirmPurchaseButtonAction(confirmPurchase);
        setCancelButtonAction(cancelPurchase);
    }


    public void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        purchaseTable.setOnMouseExited(event -> {
            // Deseleccionar la fila activa
            purchaseTable.getSelectionModel().clearSelection();
        });


        // Configura las columnas de la tabla una vez en initialize()
        colCode.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("UnitPriceAtPurchase"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityPurchased"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Configura la columna de acciones (botón eliminar)
        colActions.setCellFactory(param -> new TableCell<>() {
            final MFXButton deleteButton = new MFXButton("Eliminar");

            {
                deleteButton.setOnAction(event -> {
                    PurchaseDetailDTO selectedItem = getTableView().getItems().get(getIndex());
                    purchaseTable.getItems().remove(selectedItem);
                    purchaseTable.refresh();

                    if (selectedItem != null) {
                        purchaseTable.getItems().remove(selectedItem);
                        // Recalcula la suma total después de eliminar
                        BigDecimal totalSum = BigDecimal.ZERO; // Inicializa con 0.00

                        for (PurchaseDetailDTO purchaseDetail : purchaseTable.getItems()) {
                            totalSum = totalSum.add(purchaseDetail.getSubtotal());
                        }
                        txtTotalPurchase.setText(String.valueOf(totalSum));
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


    public void addProductDetailToTable() {
        List<String> errorMessages = new ArrayList<>();
        boolean validationSuccessful = validateTextFieldsPurchase(errorMessages);

        if (validationSuccessful) {

            long code = selectedProduct.getProductId();
            String productName = selectedProduct.getProductName();
            String brand = selectedProduct.getProductBrand();
            String description = selectedProduct.getProductDescription();
            String cost = txtCost.getText();
            String quantity = txtQuantity.getText();

            BigDecimal costDecimal = BigDecimal.valueOf(Double.parseDouble(cost)); // Convierte la cadena de costo a BigDecimal
            int quantityInt = Integer.parseInt(quantity); // Convierte la cantidad a un entero

            // Validación de los datos aquí

            // Crea un nuevo PurchaseDetailDTO
            PurchaseDetailDTO purchaseDetail = new PurchaseDetailDTO();
            purchaseDetail.setProductId(code);
            purchaseDetail.setProductName(productName);
            purchaseDetail.setProductBrand(brand);
            purchaseDetail.setProductDescription(description);
            purchaseDetail.setUnitPriceAtPurchase(BigDecimal.valueOf(Double.parseDouble(cost)));
            purchaseDetail.setQuantityPurchased(Integer.parseInt(quantity));
            purchaseDetail.setSubtotal(costDecimal.multiply(BigDecimal.valueOf(quantityInt))); // Realiza la multiplicación

            // Agrega el purchaseDetail a la lista observable de datos
            purchaseTable.getItems().add(purchaseDetail);

            // Limpia los campos de texto después de agregar los datos
            txtCost.clear();
            txtQuantity.clear();

            // Calcula la suma total en el mismo método y actualiza txtTotalPurchase
            BigDecimal totalSum = BigDecimal.ZERO; // Inicializa con 0.00
            for (PurchaseDetailDTO item : purchaseTable.getItems()) {
                totalSum = totalSum.add(item.getSubtotal());
            }
            txtTotalPurchase.setText(String.valueOf(totalSum));
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
                    "No se pudo añadir el producto a la compra: \n" + "\n" + errorMessageText,
                    "OK",
                    ev -> {
                        System.out.println("Se hizo clic en Confirmar");
                        modalDialog.close();
                    }
            );

            modalDialog.showModal(stackPane);


        }


    }


    public void confirmPurchase() {

        if (txtTotalPurchase.getText().equals("0") || txtTotalPurchase.getText().isEmpty()) {
            ModalDialog modalDialog = new ModalDialog();
            modalDialog.configureModal(
                    new Image("Images/alert.png"),
                    "Error al realizar la compra",
                    "Agrega productos a la compra: \n",
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
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);

            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setSupplierId(selectedSupplierId);/* Establece el ID del proveedor */


            // Obtener la fecha y hora actual como un objeto java.util.Date
            Date currentDate = new Date();

            //Obtener el valor total de la compra
            String totalPurchase = txtTotalPurchase.getText();


            PurchaseDTO purchaseDTO = new PurchaseDTO();
            purchaseDTO.setPurchaseDateTime(currentDate);
            purchaseDTO.setSupplier(supplierDTO);
            purchaseDTO.setUser(userDTO);
            purchaseDTO.setTotalPurchase(BigDecimal.valueOf(Double.parseDouble(totalPurchase)));


            // Crear una lista para almacenar los detalles de compra
            List<PurchaseDetailDTO> purchaseDetails = new ArrayList<>();

            // Recorrer las filas de la tabla y obtener los datos
            for (PurchaseDetailDTO row : purchaseTable.getItems()) {
                // Crear un nuevo objeto PurchaseDetailDTO en cada iteración
                PurchaseDetailDTO purchaseDetail = new PurchaseDetailDTO();
                // Obtener los datos de la fila y establecerlos en el detalle de compra
                purchaseDetail.setQuantityPurchased(row.getQuantityPurchased());
                purchaseDetail.setUnitPriceAtPurchase(row.getUnitPriceAtPurchase());
                purchaseDetail.setSubtotal(row.getSubtotal());
//            purchaseDetail.setPurchaseId(row.getPurchaseId());
                purchaseDetail.setProductId(row.getProductId());

                // Agregar el detalle de compra a la lista
                purchaseDetails.add(purchaseDetail);


            }

            // Ahora tienes una lista de detalles de compra que puedes asignar a tu objeto PurchaseDTO
            purchaseDTO.setPurchaseDetails(purchaseDetails);

            // Guardar la compra en la base de datos
            purchaseDAO.savePurchase(purchaseDTO);

            // Después de guardar la compra, actualizar el stock de los productos
            for (PurchaseDetailDTO detail : purchaseDetails) {
                productDAO.updateProductStock(detail.getProductId(), detail.getQuantityPurchased());
            }

            //Limpiar la tabla
            purchaseTable.getItems().clear();

            //Limpiar todos los campos
            cleanInputs();



            //Crear una instancia del modal
            ModalDialog modalDialog = new ModalDialog();
            // Configurar el modal mediante un solo método
            modalDialog.configureModal(new Image("Images/iconCheck.png"),
                    "Compra exitosa.",
                    "Compra realizada con exito.",
                    "Ok",
                    ev -> {
                        modalDialog.close(); // Cierra el modal
                    });

            modalDialog.showModal(stackPane);

            txtPurchaseNumber.setText(String.valueOf(purchaseDAO.getLastPurchaseId()+1));

        }


    }


    public void cleanInputs() {
        txtSearchSupplier.setText("");
        cbSupplierName.setValue(null);

        if (cbSupplierName.getValue() == null) {
            cbSupplierProducts.getItems().clear();
        }
        txtQuantity.setText("");
        txtCost.setText("");

        txtTotalPurchase.setText("");


    }




    public boolean validateTextFieldsPurchase(List<String> errorMessages) {
        boolean validationSuccessful = true;
        String costText = txtCost.getText().trim(); // Obtener el texto del campo DNI
        String quantityText = txtQuantity.getText().trim(); // Obtener el texto del campo RUC

        if (txtCost.getText().isEmpty() || txtQuantity.getText().isEmpty() || cbSupplierProducts.getValue() == null || cbSupplierName.getValue() == null) {
            errorMessages.add("Completa todos los campos.\n");
            validationSuccessful = false;
        }

        if (validationSuccessful) {

            if (!costValidator.validateCost(costText)) {
                errorMessages.add("El costo no cumple con los requisitos.\n");
                validationSuccessful = false;
            }
            if (!quantityValidator.validateQuantity(quantityText)) {
                errorMessages.add("La cantidad no cumple con los requisitos.\n");
                validationSuccessful = false;
            }

        }

        return validationSuccessful;
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
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        supplierDAO = new SupplierDAOHibernate(HibernateUtil.getSessionFactory());
        purchaseDAO = new PurchaseDAOHibernate(HibernateUtil.getSessionFactory());
        purchaseDetailDAO = new PurchaseDetailDAOHibernate(HibernateUtil.getSessionFactory());
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());

        SupplierUtil supplierUtil = new SupplierUtil(txtSearchSupplier, cbSupplierName);
        supplierUtil.startSupplierSearchAndSetSupplierName();
        supplierUtil.loadSupplierNamesToComboBox();

        costValidator = new TextFieldValidator(txtCost, labelValidateCost, TextFieldValidator.ValidationCriteria.COST);
        quantityValidator = new TextFieldValidator(txtQuantity, labelValidateQuantity, TextFieldValidator.ValidationCriteria.QUANTITY);


        // Agrega un listener a la propiedad valueProperty del ComboBox
        cbSupplierName.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // El nuevo valor seleccionado (nombre del proveedor) está en newValue
                String selectedSupplierName = newValue;

//                // Obtiene el ID del proveedor en tiempo real
                selectedSupplierId = supplierUtil.getSupplierIdByName(selectedSupplierName);

                ProductUtil productUtil = new ProductUtil(cbSupplierProducts);
                productUtil.loadProductNamesToComboBox(selectedSupplierId);


                // Puedes hacer lo que necesites con el ID del proveedor aquí
                System.out.println("Proveedor seleccionado: " + selectedSupplierName);
                System.out.println("ID del proveedor: " + selectedSupplierId);
            }
        });

        cbSupplierProducts.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // El nuevo valor seleccionado (nombre del producto) está en newValue
                String selectedProductName = newValue;

                ProductUtil productUtil = new ProductUtil(cbSupplierProducts);

                // Obtén los datos completos del producto seleccionado y guardarlo en el DTO
                selectedProduct = productUtil.getProductByName(selectedProductName);

                if (selectedProduct != null) {
                    // Haz lo que necesites con los datos del producto seleccionado
                    System.out.println("Producto seleccionado: " + selectedProductName);
                    System.out.println("ID del producto: " + selectedProduct.getProductId());
                    System.out.println("Descripción: " + selectedProduct.getProductDescription());
                    // ... y otros campos del producto
                } else {
                    // Maneja el caso en el que no se encuentra el producto
                    System.out.println("Producto no encontrado.");
                }
            }
        });


        configureTable();

        txtPurchaseNumber.setText(String.valueOf(purchaseDAO.getLastPurchaseId()+1));


    }

}
