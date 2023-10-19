package com.tienda.ControllerGUI.Sale;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.*;
import com.tienda.Dao.SaleDAO;
import com.tienda.DaoImpl.SaleDAOHibernate;
import com.tienda.Tools.PDFGenerator;
import com.tienda.Utils.SaleDataLoadingUtil;
import com.tienda.dto.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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

    SaleDataLoadingUtil saleDataLoadingUtil;



    @FXML
    void handleLoadDate(ActionEvent event) {
        saleTable.getItems().clear();
        saleDataLoadingUtil.loadSaleTableData(saleTable);

    }

    @FXML
    void handleNewSale(ActionEvent event) {

        //Crear una instancia del modal
        ModalSale modalSale = new ModalSale();
        // Configurar el modal mediante un solo método
        modalSale.configureModal(
                ev1-> {
                    modalSale.handleNewCustomer(root);

                },

                ev2-> {
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
                    modalSale.close();
                }

                );

        modalSale.showModal(root);


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
        // Configura la columna "colSupplier" para mostrar el nombre del proveedor en lugar del objeto SupplierDTO.
        colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerDni()));
        // Configura la columna "colUser" para mostrar el número de DNI del usuario en lugar del objeto UserDTO.
        colUser.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUserDni()));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountTotal"));
        colTotalSale.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Configura la columna de acciones
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button showButton = new Button("Ver detalle");

            private final Button deleteButton = new Button("Eliminar");

            private final HBox buttons = new HBox(showButton,deleteButton);


            {
                // Asigna la clase CSS para centrar los botones
                buttons.getStyleClass().add("centered-buttons");
                showButton .getStyleClass().add("buttonA");
                deleteButton .getStyleClass().add("buttonB");


                showButton.setOnAction(event -> {
                    ModalSaleDetail modalSaleDetail= new ModalSaleDetail();
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

//    @FXML
//    void handleNewDocument(ActionEvent event) {
//        // Crea una instancia de DocumentDTO
//        DocumentDTO document = new DocumentDTO();
//
//        // Configura los atributos de DocumentDTO
//        document.setDocumentId(1L);
//        document.setDocumentNumber("F2023-001");
//        document.setIssueDate(new Date());
//        document.setSubtotal(new BigDecimal("250.00"));
//        document.setTotalDiscount(new BigDecimal("30.00"));
//        document.setIgvAmount(new BigDecimal("0.18"));
//        document.setTotalAmount(new BigDecimal("295.00"));
//
//        // Crea una instancia de DocumentTypeDTO con datos ficticios
//        DocumentTypeDTO documentType = new DocumentTypeDTO();
//        documentType.setDocumentTypeName("Boleta");
//        document.setDocumentType(documentType);
//
//        // Crea una instancia de SaleDTO con datos ficticios
//        SaleDTO sale = new SaleDTO();
//        sale.setSaleId(1L);
//        sale.setSaleDateTime(new Date());
//
//        // Crea una lista de detalles de venta (productos vendidos) con datos ficticios
//        List<SaleDetailDTO> saleDetails = new ArrayList<>();
//
//        SaleDetailDTO saleDetail1 = new SaleDetailDTO();
//        saleDetail1.setDetailId(1L);
//        saleDetail1.setQuantitySold(2);
////        saleDetail1.setDiscountPerProduct(new BigDecimal("10.00"));
//        saleDetail1.setSubtotalPerProduct(new BigDecimal("90.00"));
//
//        // Crea una instancia de ProductDTO con datos ficticios
//        ProductDTO product1 = new ProductDTO();
//        product1.setProductId(1L);
//        product1.setProductName("Producto 1");
//        product1.setUnitPrice(new BigDecimal("266666.00"));
//        // Configura otros atributos del producto
//
//        // Asocia el producto al detalle de venta
//        saleDetail1.setProduct(product1);
//
//        // Agrega el detalle de venta a la lista de detalles
//        saleDetails.add(saleDetail1);
//
//        // Repite el proceso para otros detalles de venta (productos) si es necesario
//
//        // Asigna la lista de detalles de venta a la venta
//        sale.setSaleDetails(saleDetails);
//
//        // Asigna una instancia de CustomerDTO con datos ficticios a la venta
//        CustomerDTO customer = new CustomerDTO();
//        customer.setCustomerDni("23333");
//        customer.setCustomerFirstName("Alexander Bryan");
//        customer.setCustomerLastName("Onocc Navarro");
//        // Configura otros atributos del cliente
//        document.setCustomer(customer);
//
//        // Asigna una instancia de UserDTO con datos ficticios a la venta
//        UserDTO user = new UserDTO();
//        // Configura atributos del usuario
//        document.setUser(user);
//
//        // Asigna la instancia de SaleDTO a DocumentDTO
//        document.setSale(sale);
//
//        // Genera el PDF utilizando PDFGenerator
//        PDFGenerator pdfGenerator = new PDFGenerator();
//        pdfGenerator.generatePDF(document);
//    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        saleDAO= new SaleDAOHibernate(HibernateUtil.getSessionFactory());
        saleDataLoadingUtil=new SaleDataLoadingUtil(saleDAO);
        saleDataLoadingUtil.loadSaleTableData(saleTable);

    }
}
