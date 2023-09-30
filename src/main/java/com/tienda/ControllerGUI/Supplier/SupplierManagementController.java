package com.tienda.ControllerGUI.Supplier;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.ControllerGUI.Components.ModalSupplier;
import com.tienda.Dao.SupplierDAO;
import com.tienda.Dao.SupplierDAOHibernate;
import com.tienda.Exception.SupplierException;
import com.tienda.Utils.SupplierDataLoadingUtil;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierManagementController implements Initializable {
    @FXML
    private StackPane root;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private TableColumn<SupplierDTO, Void> colActions;

    @FXML
    private TableColumn<SupplierDTO, String> colAddress;

    @FXML
    private TableColumn<SupplierDTO, String> colCodigo;

    @FXML
    private TableColumn<SupplierDTO, String> colEmail;

    @FXML
    private TableColumn<SupplierDTO, String> colPhoneNumber;

    @FXML
    private TableColumn<SupplierDTO, String> colRuc;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierName;

    @FXML
    private MFXLegacyTableView<SupplierDTO> supplierTable;

    @FXML
    private MFXTextField txtfSearch;

    SupplierDAO supplierDAO;
    SupplierDataLoadingUtil supplierDataLoadingUtil;


    @FXML
    void handleAddSupplier() {
        //Crear una instancia del modal
        ModalSupplier modalSupplier = new ModalSupplier();
        // Configurar el modal mediante un solo método
        modalSupplier.configureModal(
                ev -> {

                    List<String> errorMessages = new ArrayList<>();
                    boolean validationSuccessful = modalSupplier.validateTextFieldsSupplier(errorMessages);

                    if (validationSuccessful) {
                        // Obtener el RUC y el correo electrónico ingresados en el formulario
                        String ruc = modalSupplier.getTxtSupplierRuc();
                        String email = modalSupplier.getTxtSupplierEmail();

                        // Verificar si ya existe un proveedor con el mismo RUC
                        if (supplierDAO.existsSupplierWithRuc(ruc)) {
                            // Mostrar un mensaje de error si el proveedor ya existe con el mismo RUC
                            errorMessages.add("El proveedor ya existe con este RUC.");
                        }

                        // Verificar si ya existe un proveedor con el mismo correo electrónico
                        if (supplierDAO.existsSupplierWithEmail(email)) {
                            // Mostrar un mensaje de error si el proveedor ya existe con el mismo correo electrónico
                            errorMessages.add("El proveedor ya existe con este correo electrónico.");
                        }


                        // Si no hay errores, proceder a guardar el proveedor
                        if (errorMessages.isEmpty()) {


                            // La validación fue exitosa, procede a crear el proveedor
                            SupplierDTO newSupplierDTO = new SupplierDTO();
                            newSupplierDTO.setSupplierName(modalSupplier.getTxtSupplierName());
                            newSupplierDTO.setSupplierRuc(modalSupplier.getTxtSupplierRuc());
                            newSupplierDTO.setSupplierAddress(modalSupplier.getTxtSupplierAddress());
                            newSupplierDTO.setSupplierEmail(modalSupplier.getTxtSupplierEmail());
                            newSupplierDTO.setSupplierPhoneNumber(modalSupplier.getTxtSupplierPhoneNumber());
                            // Intenta registrar al proveedor
                            supplierDAO.saveSupplier(newSupplierDTO);


                            // Muestra un mensaje de éxito si es necesario
                            //Crear una instancia del modal
                            ModalDialog modalDialog = new ModalDialog();
                            // Configurar el modal mediante un solo método
                            modalDialog.configureModal(
                                    new Image("Images/iconCheck.png"),
                                    "Registrado correctamente.",
                                    "Proveedor registrado correctamente.",
                                    "OK",
                                    e -> {
                                        // Lógica cuando se hace clic en el botón "Confirmar"
                                        System.out.println("Se hizo clic en Confirmar");
                                        modalDialog.close();
                                        modalSupplier.close();
                                    }
                            );
                            modalDialog.showModal(root);

                            supplierDataLoadingUtil.loadSupplierTableData(supplierTable);


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
                                    "No se pudo registrar el proveedor correctamente: \n" + "\n" + errorMessageText,
                                    "OK",
                                    e -> {
                                        System.out.println("Se hizo clic en Confirmar");
                                        modalDialog.close();
                                    }
                            );

                            modalDialog.showModal(root);
                            System.out.println("No se pudo crear el proveedor debido a errores de validación.");
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
                                "No se pudo registrar el proveedor correctamente: \n" + "\n" + errorMessageText,
                                "OK",
                                e -> {
                                    // Lógica cuando se hace clic en el botón "Confirmar"
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();
                                }
                        );

                        modalDialog.showModal(root);
                        // También puedes mostrar un mensaje de error general si lo deseas
                        System.out.println("No se pudo crear el proveedor debido a errores de validación.");
                    }

                },
                ev -> {
                    modalSupplier.close(); // Cierra el modal
                }


        );

        modalSupplier.showModal(root);
    }


    @FXML
    void handleLoadDate() {
        supplierTable.getItems().clear();
        // Carga los datos en la TableView al inicializar la ventana
        supplierDataLoadingUtil.loadSupplierTableData(supplierTable);
    }


    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        supplierTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                supplierTable.getSelectionModel().clearSelection();
            }
        });


        // Configura las celdas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("supplierRuc"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("supplierAddress"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("supplierPhoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("supplierEmail"));

        // Configura la columna de acciones
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox buttons = new HBox(editButton, deleteButton);


            {
                // Asigna la clase CSS para centrar los botones
                buttons.getStyleClass().add("centered-buttons");
                editButton.getStyleClass().add("buttonA");
                deleteButton.getStyleClass().add("buttonB");


                editButton.setOnAction(event -> {

                    SupplierDTO supplierDTO = getTableView().getItems().get(getIndex());
                    // Lógica para editar el usuarioalex
                    System.out.println("Editar producto: " + supplierDTO.getSupplierId());

                    ModalSupplier modalSupplier = new ModalSupplier();
                    //Setea los campos del proveedor en el modal
                    modalSupplier.setTxtSupplierName(supplierDTO.getSupplierName());
                    modalSupplier.setTxtSupplierRuc(supplierDTO.getSupplierRuc());
                    modalSupplier.setTxtSupplierAddress(supplierDTO.getSupplierAddress());
                    modalSupplier.setTxtSupplierPhoneNumber(supplierDTO.getSupplierPhoneNumber());
                    modalSupplier.setTxtSupplierEmail(supplierDTO.getSupplierEmail());
                    // Configurar el modal
                    modalSupplier.configureModal("Actualizar proveedor",
                            "Completa con los campos necesarios para actualizar un proveedor.",
                            "Actualizar",
                            e -> {


                                //Crea un DTO de supplier
                                SupplierDTO updateSupplier = new SupplierDTO();
                                updateSupplier.setSupplierId(supplierDTO.getSupplierId());
                                updateSupplier.setSupplierName(modalSupplier.getTxtSupplierName());
                                updateSupplier.setSupplierRuc(modalSupplier.getTxtSupplierRuc());
                                updateSupplier.setSupplierAddress(modalSupplier.getTxtSupplierAddress());
                                updateSupplier.setSupplierPhoneNumber(modalSupplier.getTxtSupplierPhoneNumber());
                                updateSupplier.setSupplierEmail(modalSupplier.getTxtSupplierEmail());
                                supplierDAO.updateSupplier(updateSupplier);


                                ModalDialog modalDialog = new ModalDialog();
                                modalDialog.configureModal(
                                        new Image("Images/iconCheck.png"),
                                        "Actualizado correctamente.",
                                        "Proveedor actualizado correctamente.",
                                        "OK",
                                        es -> {
                                            System.out.println("Se hizo clic en Confirmar");
                                            modalDialog.close();
                                            modalSupplier.close();
                                        }
                                );
                                modalDialog.showModal(root);
                                supplierDataLoadingUtil.loadSupplierTableData(supplierTable);



                            },
                            e -> {
                                // Lógica cuando se hace clic en el botón "Cancelar"
                                System.out.println("Se hizo clic en Cancelar");
                                modalSupplier.close();
                            }

                    );


                    modalSupplier.showModal(root);


                });


                deleteButton.setOnAction(event -> {

                    SupplierDTO supplierDTO = getTableView().getItems().get(getIndex());
                    // Lógica para eliminar el usuario
                    System.out.println("Eliminar usuario: " + supplierDTO.getSupplierId());
                    supplierDAO.deleteSupplier(supplierDTO.getSupplierId());
                    //Actualizar la tabla con el proveedor eliminado
                    handleLoadDate();


                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/iconCheck.png"),
                            "Proveedor eliminado correctamente.",
                            "El proveedor con DNI:  " + supplierDTO.getSupplierRuc() + " ha sido eliminado correctamente",
                            "Ok",
                            ev -> {
                                modalDialog.close(); // Cierra el modal
                            });

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
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        supplierDAO = new SupplierDAOHibernate(HibernateUtil.getSessionFactory());

        configureTable();
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        supplierDataLoadingUtil = new SupplierDataLoadingUtil(supplierDAO);
        // Carga los datos en la TableView al inicializar la ventana
        supplierDataLoadingUtil.loadSupplierTableData(supplierTable);

    }
}
