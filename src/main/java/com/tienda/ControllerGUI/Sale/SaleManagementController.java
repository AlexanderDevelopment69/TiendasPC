package com.tienda.ControllerGUI.Sale;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.*;
import com.tienda.Dao.DocumentDAO;
import com.tienda.Dao.ProductDAO;
import com.tienda.Dao.SaleDAO;
import com.tienda.DaoImpl.DocumentDAOHibernate;
import com.tienda.DaoImpl.SaleDAOHibernate;
import com.tienda.Tools.PDFGenerator;
import com.tienda.Utils.DocumentUtil;
import com.tienda.Utils.SaleDataLoadingUtil;
import com.tienda.dto.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.event.ChangeListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SaleManagementController implements Initializable {

    @FXML
    private MFXLegacyTableView<SaleDTO> saleTable;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private TableColumn<SaleDTO, Void> colActions;

    @FXML
    private TableColumn<SaleDTO, String> colCode;

    @FXML
    private TableColumn<SaleDTO, String> colCustomer;

    @FXML
    private TableColumn<SaleDTO, String> colDateTime;

    @FXML
    private TableColumn<SaleDTO, String> colTotalSale;

    @FXML
    private TableColumn<SaleDTO, String> colDiscount;

    @FXML
    private TableColumn<SaleDTO, String> colSubtotal;


    @FXML
    private TableColumn<SaleDTO, String> colUser;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField txtSearch;

    SaleDAO saleDAO;
    DocumentDAO documentDAO;

    SaleDataLoadingUtil saleDataLoadingUtil;
    DocumentUtil documentUtil;


    @FXML
    void handleLoadDate(ActionEvent event) {
        saleTable.getItems().clear();
        saleDataLoadingUtil.loadSaleTableData(saleTable);

    }

    @FXML
    void handleNewSale(ActionEvent event) {

        // Crear un nuevo Stage para el modal
        Stage modalStage = new Stage();


        //Crear una instancia del modal
        ModalSale modalSale = new ModalSale();
        // Configurar el modal mediante un solo método
        modalSale.configureModal(
                ev1 -> {
                    modalSale.handleNewCustomer(modalSale);
                },

                ev2 -> {
                    modalSale.addProductDetailToTable();
                },
                ev3 -> {
                    modalSale.cleanInputs();
                },
                ev4 -> {
                    modalSale.confirmSale();
                    saleDataLoadingUtil.loadSaleTableData(saleTable);

                },
                ev5 -> {
//                    modalSale.close();
                    modalStage.close();
                }

        );


        // Usar un StackPane dentro del modal para que se expanda
        StackPane modalRoot = new StackPane();
        Scene modalScene = new Scene(modalRoot);

        modalStage.setScene(modalScene);

        // Mostrar el modal en el nuevo Stage
        modalSale.showModal(modalRoot);
        modalStage.show();

        // Vincular el tamaño del StackPane del modal al tamaño del Stage
        modalRoot.prefWidthProperty().bind(modalStage.widthProperty());
        modalRoot.prefHeightProperty().bind(modalStage.heightProperty());


    }


    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        saleTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                saleTable.getSelectionModel().clearSelection();
            }
        });


        // Configura las celdas de la tabla
        colCode.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("saleDateTime"));
//        // Configura la columna "colSupplier" para mostrar el nombre del proveedor en lugar del objeto SupplierDTO.
//        colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerDni()));

        // Configura la columna "colUser" para mostrar el número de DNI del usuario en lugar del objeto UserDTO.
        colUser.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUserDni()));

        colCustomer.setCellValueFactory(cellData -> {
            String customerDni = cellData.getValue().getCustomer().getCustomerDni();
            String customerRuc = cellData.getValue().getCustomer().getCustomerRuc();

            if (customerDni != null && customerRuc != null) {
                return new SimpleStringProperty("DNI: "+customerDni);
            }
            if (customerRuc != null) {
                return new SimpleStringProperty("RUC: "+customerRuc);
            }
            if (customerDni != null) {
                return new SimpleStringProperty("DNI: "+customerDni);
            }

            return new SimpleStringProperty("N/A"); // Otra alternativa si ambos son nulos

        });

        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountTotal"));
        colTotalSale.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Configura la columna de acciones
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button documentButton = new Button("Descargar");
            private final Button showButton = new Button("Ver detalle");

            private final Button deleteButton = new Button("Eliminar");

            private final HBox buttons = new HBox(documentButton, showButton, deleteButton);


            {
                // Asigna la clase CSS para centrar los botones
                buttons.getStyleClass().add("centered-buttons");
                documentButton.getStyleClass().add("buttonA");
                showButton.getStyleClass().add("buttonA");
                deleteButton.getStyleClass().add("buttonB");


                documentButton.setOnAction(event -> {
                    SaleDTO saleDTO = getTableView().getItems().get(getIndex());
                    System.out.println(saleDTO.getSaleId());
                    DocumentDTO document= documentDAO.getDocumentBySaleId(saleDTO.getSaleId());



                    System.out.println("Document number: "+document.getDocumentNumber());
                    System.out.println("Tipo de documento: "+document.getDocumentType().getDocumentTypeName());
                    System.out.println("Fecha: "+document.getIssueDate());
//                     Obtener la lista de detalles de venta (productos)
                    List<SaleDetailDTO> saleDetails = document.getSaleDetails();

                    for (SaleDetailDTO saleDetail : saleDetails) {
                        System.out.println("Cantidad: "+saleDetail.getQuantitySold());
                        System.out.println("ProductName: "+saleDetail.getProduct().getProductName());
                        System.out.println("Precio"+saleDetail.getProduct().getUnitPrice());
                        System.out.println("SubTotal: "+saleDetail.getSubtotalPerProduct());

                    }

                    System.out.println("Cliente: " + (document.getCustomer().getCustomerDni() == null ? document.getCustomer().getCustomerRuc() : document.getCustomer().getCustomerDni()));
                    System.out.println("Usuario: "+document.getUser().getUserName().concat(" "+document.getUser().getUserLastName()));
                    System.out.println("SubTotal: "+ document.getSubtotal());
                    System.out.println("Descuento Total: "+document.getTotalDiscount());
                    System.out.println("IGV: "+document.getIgvAmount());
                    System.out.println("Total: "+ document.getTotalAmount());



//                    // Genera el PDF utilizando PDFGenerator
                    PDFGenerator pdfGenerator = new PDFGenerator();
                    pdfGenerator.generatePDF(document);


                });


                showButton.setOnAction(event -> {
                    ModalSaleDetail modalSaleDetail = new ModalSaleDetail();
                    SaleDTO saleDTO = getTableView().getItems().get(getIndex());
                    modalSaleDetail.loadingDataToTable(saleDTO.getSaleId());
                    modalSaleDetail.configureModal(
                            e -> {
                                modalSaleDetail.close();
                            }
                    );
                    modalSaleDetail.showModal(root);


                });


                deleteButton.setOnAction(event -> {
                    SaleDTO saleDTO = getTableView().getItems().get(getIndex());

                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/delete.png"),
                            "Eliminar la venta.",
                            "Elige una opcion.",
                            "Eliminar",
                            "Cancelar",
                            ev1 -> {
                                //Eliminar la compra
                                saleDAO.deleteSale(saleDTO.getSaleId());
                                saleDataLoadingUtil.loadSaleTableData(saleTable);
                                modalDialog.close(); // Cierra el modal
                                //Crear una instancia del modal
                                ModalDialog modalDialog2 = new ModalDialog();
                                // Configurar el modal mediante un solo método
                                modalDialog2.configureModal(new Image("Images/iconCheck.png"),
                                        "Venta eliminada.",
                                        "Venta eliminada con exito.",
                                        "Ok",
                                        ev -> {

                                            modalDialog2.close();


                                        });

                                modalDialog2.showModal(root);


                            },
                            ev2 -> {
                                modalDialog.close(); // Cierra el modal
                            }
                    );

                    modalDialog.showModal(root);

//


                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        saleDAO = new SaleDAOHibernate(HibernateUtil.getSessionFactory());
        documentDAO= new DocumentDAOHibernate(HibernateUtil.getSessionFactory());


        saleDataLoadingUtil = new SaleDataLoadingUtil(saleDAO);
        saleDataLoadingUtil.loadSaleTableData(saleTable);


        documentUtil= new DocumentUtil(documentDAO);



    }
}
