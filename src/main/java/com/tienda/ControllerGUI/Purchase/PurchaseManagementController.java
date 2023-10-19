package com.tienda.ControllerGUI.Purchase;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.*;
import com.tienda.Dao.PurchaseDAO;
import com.tienda.DaoImpl.PurchaseDAOHibernate;
import com.tienda.Utils.CustomerDataLoadingUtil;
import com.tienda.Utils.PurchaseDataLoadingUtil;
import com.tienda.Utils.PurchaseDetailDataLoadingUtil;
import com.tienda.Utils.PurchaseDetailUtil;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseManagementController implements Initializable {

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private TableColumn<PurchaseDTO, Void> colActions;

    @FXML
    private TableColumn<PurchaseDTO, String> colCode;

    @FXML
    private TableColumn<PurchaseDTO, String> colDateTime;

    @FXML
    private TableColumn<PurchaseDTO, String> colSupplier;

    @FXML
    private TableColumn<PurchaseDTO, String> colTotalPurchase;

    @FXML
    private TableColumn<PurchaseDTO, String> colUser;

    @FXML
    private MFXLegacyTableView<PurchaseDTO> purchaseTable;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField txtfSearch;

    PurchaseDAO purchaseDAO;
    PurchaseDataLoadingUtil purchaseDataLoadingUtil;

    @FXML
    void handleAddPurchase(ActionEvent event) {
        //Crear una instancia del modal
        ModalPurchase modalPurchase = new ModalPurchase();
        // Configurar el modal mediante un solo método
        modalPurchase.configureModal(
                ev1 -> {
                    modalPurchase.addProductDetailToTable();
                },
                ev2 -> {
                    modalPurchase.cleanInputs();
                },
                ev3 -> {
                    modalPurchase.confirmPurchase();
                    purchaseDataLoadingUtil.loadPurchaseTableData(purchaseTable);
                },
                ev4 -> {
                    modalPurchase.close(); // Cierra el modal
                }

        );

        modalPurchase.showModal(root);


    }

    @FXML
    void handleLoadDate(ActionEvent event) {
        purchaseTable.getItems().clear();
        purchaseDataLoadingUtil.loadPurchaseTableData(purchaseTable);
    }



    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        purchaseTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                purchaseTable.getSelectionModel().clearSelection();
            }
        });


        // Configura las celdas de la tabla
        colCode.setCellValueFactory(new PropertyValueFactory<>("purchaseId"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("purchaseDateTime"));
        // Configura la columna "colSupplier" para mostrar el nombre del proveedor en lugar del objeto SupplierDTO.
        colSupplier.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getSupplierName()));
        // Configura la columna "colUser" para mostrar el número de DNI del usuario en lugar del objeto UserDTO.
        colUser.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUserDni()));
        colTotalPurchase.setCellValueFactory(new PropertyValueFactory<>("totalPurchase"));

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
                    ModalPurchaseDetail modalPurchaseDetail= new ModalPurchaseDetail();
                    PurchaseDTO purchaseDTO = getTableView().getItems().get(getIndex());
                    modalPurchaseDetail.loadingDataToTable(purchaseDTO.getPurchaseId());
                    modalPurchaseDetail.configureModal(
                            e -> {
                                    modalPurchaseDetail.close();
                            }
                    );
                    modalPurchaseDetail.showModal(root);


                });


                deleteButton.setOnAction(event -> {
                    PurchaseDTO purchaseDTO = getTableView().getItems().get(getIndex());

                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/delete.png"),
                            "Eliminar la compra.",
                            "Elige una opcion.",
                            "Eliminar",
                            "Cancelar",
                            ev1 -> {
                                //Eliminar la compra
                                purchaseDAO.deletePurchase(purchaseDTO.getPurchaseId());
                                purchaseDataLoadingUtil.loadPurchaseTableData(purchaseTable);
                                modalDialog.close(); // Cierra el modal
                                //Crear una instancia del modal
                                ModalDialog modalDialog2 = new ModalDialog();
                                // Configurar el modal mediante un solo método
                                modalDialog2.configureModal(new Image("Images/iconCheck.png"),
                                        "Compra eliminada.",
                                        "Compra eliminada con exito.",
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
        purchaseDAO= new PurchaseDAOHibernate(HibernateUtil.getSessionFactory());
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        purchaseDataLoadingUtil = new PurchaseDataLoadingUtil(purchaseDAO);
        purchaseDataLoadingUtil.loadPurchaseTableData(purchaseTable);

        configureTable();

//
//        // Supongamos que tienes una instancia válida de PurchaseDAO llamada purchaseDAO
//
//// ID de la compra de la que deseas obtener los detalles
//        long purchaseId = 11; // Reemplaza esto con el ID de tu compra real
//
//// Llama al método para obtener los detalles de compra
//        List<PurchaseDetailDTO> purchaseDetails = purchaseDAO.getPurchaseDetailsByPurchaseId(purchaseId);
//
//// Ahora puedes trabajar con la lista de detalles de compra
//        for (PurchaseDetailDTO detail : purchaseDetails) {
//            System.out.println(detail);
//        }



    }
}
