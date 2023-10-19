package com.tienda.ControllerGUI.Customer;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalCustomer;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.Dao.CustomerDAO;
import com.tienda.DaoImpl.CustomerDAOHibernate;
import com.tienda.Utils.CustomerDataLoadingUtil;
import com.tienda.Utils.CustomerUtil;
import com.tienda.dto.CustomerDTO;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerManagementController implements Initializable {


    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private TableColumn<CustomerDTO, Void> colActions;

    @FXML
    private TableColumn<CustomerDTO, String> colLastNames;

    @FXML
    private TableColumn<CustomerDTO, String> colCode;

    @FXML
    private TableColumn<CustomerDTO, String> colDni;
    @FXML
    private TableColumn<CustomerDTO, String> colRuc;

    @FXML
    private TableColumn<CustomerDTO, String> colEmail;

    @FXML
    private TableColumn<CustomerDTO, String> colNames;

    @FXML
    private MFXLegacyTableView<CustomerDTO> customerTable;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField txtSearch;

    CustomerDAO customerDAO;
    CustomerDataLoadingUtil customerDataLoadingUtil;

    CustomerUtil customerUtil;

    @FXML
    void handleAddCustomer() {

        //Crear una instancia del modal
        ModalCustomer modalCustomer = new ModalCustomer();
        // Configurar el modal mediante un solo método
        modalCustomer.configureModal("Nuevo cliente",
                "Completa los campos para registrar un nuevo cliente", "Registrar",
                ev1 -> {

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
                                        // Lógica cuando se hace clic en el botón "Confirmar"
                                        System.out.println("Se hizo clic en Confirmar");
                                        modalDialog.close();
                                        modalCustomer.close();
                                    }
                            );
                            modalDialog.showModal(root);


                            customerDataLoadingUtil.loadCustomerTableData(customerTable);
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
                ev2 -> {
                    modalCustomer.close(); // Cierra el modal
                }


        );

        modalCustomer.showModal(root);


    }

    @FXML
    void handleLoadDate(ActionEvent event) {
        customerTable.getItems().clear();
        // Carga los datos en la TableView
        customerDataLoadingUtil.loadCustomerTableData(customerTable);
    }


    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        customerTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                customerTable.getSelectionModel().clearSelection();
            }
        });


        // Configura las celdas de la tabla
        colCode.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("customerDni"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("customerRuc"));
        colLastNames.setCellValueFactory(new PropertyValueFactory<>("customerLastName"));
        colNames.setCellValueFactory(new PropertyValueFactory<>("customerFirstName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));

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

                    CustomerDTO customerDTO = getTableView().getItems().get(getIndex());
                    // Lógica para editar un cliente
                    System.out.println("Editar cliente: " + customerDTO.getCustomerId());

                    ModalCustomer modalCustomer = new ModalCustomer();
                    //Setea los campos del cliente en el modal
                    modalCustomer.txtCustomerDni.setText(customerDTO.getCustomerDni() == null ? "" : customerDTO.getCustomerDni());
                    modalCustomer.txtCustomerRuc.setText(customerDTO.getCustomerRuc() == null ? "" : customerDTO.getCustomerRuc());
                    modalCustomer.txtCustomerNames.setText(customerDTO.getCustomerFirstName());
                    modalCustomer.txtCustomerLastNames.setText(customerDTO.getCustomerLastName());
                    modalCustomer.txtCustomerAdress.setText(customerDTO.getCustomerAddress());
                    modalCustomer.txtCustomerPhoneNumber.setText(customerDTO.getCustomerPhoneNumber());
                    modalCustomer.txtCustomerEmail.setText(customerDTO.getCustomerEmail() == null ? "" : customerDTO.getCustomerEmail());

                    System.out.println(customerDTO.getCustomerDni());
                    System.out.println(customerDTO.getCustomerRuc());

                    // Configurar el modal
                    modalCustomer.configureModal("Actualizar cliente",
                            "Completa con los campos necesarios para actualizar al cliente.",
                            "Actualizar",
                            e -> {

                                //Crea un DTO de Customer
                                CustomerDTO updatecustomer = new CustomerDTO();
                                updatecustomer.setCustomerId(customerDTO.getCustomerId());
                                updatecustomer.setCustomerDni(modalCustomer.txtCustomerDni.getText().trim());
                                updatecustomer.setCustomerRuc(modalCustomer.txtCustomerRuc.getText().trim());
                                updatecustomer.setCustomerLastName(modalCustomer.txtCustomerLastNames.getText().trim());
                                updatecustomer.setCustomerFirstName(modalCustomer.txtCustomerNames.getText().trim());
                                updatecustomer.setCustomerAddress(modalCustomer.txtCustomerAdress.getText().trim());
                                updatecustomer.setCustomerEmail(modalCustomer.txtCustomerEmail.getText().trim());
                                updatecustomer.setCustomerPhoneNumber(modalCustomer.txtCustomerPhoneNumber.getText().trim());
                                customerDAO.updateCustomer(updatecustomer);

                                ModalDialog modalDialog = new ModalDialog();
                                modalDialog.configureModal(
                                        new Image("Images/iconCheck.png"),
                                        "Actualizado correctamente.",
                                        "Cliente actualizado correctamente.",
                                        "OK",
                                        es -> {
                                            System.out.println("Se hizo clic en Confirmar");
                                            modalDialog.close();
                                            modalCustomer.close();
                                        }
                                );
                                modalDialog.showModal(root);

                                customerDataLoadingUtil.loadCustomerTableData(customerTable);


                            },
                            e -> {
                                // Lógica cuando se hace clic en el botón "Cancelar"
                                System.out.println("Se hizo clic en Cancelar");
                                modalCustomer.close();
                            }

                    );


                    modalCustomer.showModal(root);


                });


                deleteButton.setOnAction(event -> {

                    CustomerDTO customerDTO = getTableView().getItems().get(getIndex());
                    // Lógica para eliminar un cliente
                    System.out.println("Eliminar cliente: " + customerDTO.getCustomerId());
                    customerDAO.deleteCustomer(customerDTO.getCustomerId());
                    //Actualizar la tabla con el cliente eliminado
                    customerDataLoadingUtil.loadCustomerTableData(customerTable);


                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/iconCheck.png"),
                            "Cliente eliminado correctamente.",
                            "El cliente con DNI:  " + customerDTO.getCustomerId() + " ha sido eliminado correctamente",
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
        customerDAO = new CustomerDAOHibernate(HibernateUtil.getSessionFactory());

        configureTable();
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        customerDataLoadingUtil = new CustomerDataLoadingUtil(customerDAO);
        // Carga los datos en la TableView al inicializar la ventana
        customerDataLoadingUtil.loadCustomerTableData(customerTable);

        customerUtil= new CustomerUtil(txtSearch,customerTable);
        customerUtil.startCustomerSearch();

    }
}
