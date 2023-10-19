package com.tienda.ControllerGUI.User;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.Modal;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.Dao.UserDAO;
import com.tienda.DaoImpl.UserDAOHibernate;
import com.tienda.Utils.UserDataLoadingUtil;
import com.tienda.Utils.UserUtil;
import com.tienda.dto.UserDTO;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

public class UserManagementController implements Initializable {

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField textSearch;

    @FXML
    private MFXLegacyTableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, String> colCodigo;

    @FXML
    private TableColumn<UserDTO, String> colDni;

    @FXML
    private TableColumn<UserDTO, String> colApellidos;

    @FXML
    private TableColumn<UserDTO, String> colNombres;

    @FXML
    private TableColumn<UserDTO, String> colEmail;

    @FXML
    private TableColumn<UserDTO, String> colRol;

    @FXML
    private TableColumn<UserDTO, Void> colActions;

    private UserDAO userDAO;

    private UserDataLoadingUtil dataLoadingUtil;


    private boolean isDataLoading = false; // Controla si la carga de datos está en curso


    private UserUtil userUtil;

    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        userTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                userTable.getSelectionModel().clearSelection();
            }
        });


        // Configura las celdas de la tablaalex
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("userDni"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("userLastName"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("roleName"));

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
                    UserDTO userDTO = getTableView().getItems().get(getIndex());
                    // Lógica para editar el usuarioalex
                    System.out.println("Editar usuario: " + userDTO.getUserId());


                    Modal modal = new Modal();
                    modal.addElementRol("Administrador");
                    modal.addElementRol("Vendedor");
                    // Configurar el modal mediante un solo método
                    modal.configureModal(
                            userDTO.getRoleName(),
                            e -> {
                                // Lógica cuando se hace clic en el botón "Confirmar"
                                System.out.println("Click mouse");
                                if (modal.getValueSelectedRol() == null) {
                                    return;
                                }
                                if (modal.getValueSelectedRol().equals("Administrador")) {
                                    userDAO.assignUserRole(userDTO.getUserId(), Long.valueOf(1));

                                }
                                if (modal.getValueSelectedRol().equals("Vendedor")) {
                                    userDAO.assignUserRole(userDTO.getUserId(), Long.valueOf(2));
                                }

                                modal.close();

                                //Crear una instancia del modal
                                ModalDialog modalDialog = new ModalDialog();
                                // Configurar el modal mediante un solo método
                                modalDialog.configureModal(new Image("Images/iconCheck.png"),
                                        "El usuario ha sido actualizado correctamente.",
                                        "El usuario con DNI: " + userDTO.getUserDni() + " ha cambiado su rol a: " + modal.getValueSelectedRol(),
                                        "Ok",
                                        ev -> {
                                            modalDialog.close(); // Cierra el modal
                                        });

                                modalDialog.showModal(root);


                                //Actualiza la tabla
                                handleLoadDate();

                            },
                            e -> {
                                // Lógica cuando se hace clic en el botón "Cancelar"
                                System.out.println("Se hizo clic en Cancelar");
                                modal.close();
                            }
                    );


                    modal.showModal(root);


                });


                deleteButton.setOnAction(event -> {
                    UserDTO userDTO = getTableView().getItems().get(getIndex());
                    // Lógica para eliminar el usuario
                    System.out.println("Eliminar usuario: " + userDTO.getUserId());
                    userDAO.deleteUser(userDTO.getUserId());

                    //Actualiza la tabla
                    handleLoadDate();


                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/iconCheck.png"),
                            "Usuario eliminado correctamente.",
                            "El usuario con DNI:  " + userDTO.getUserDni() + " ha sido eliminado correctamente",
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


//    public void loadUserTableData() {
//
//        // Verificar si la carga de datos ya está en curso
//        if (isDataLoading) {
//            // El hilo ya se ha iniciado, no hacer nada adicional
//            return;
//        }
//
//        isDataLoading = true; // Marcar que la carga de datos está en curso
//
//            Service<ObservableList<User>> dataLoadingService = new Service<>() {
//                @Override
//                protected Task<ObservableList<User>> createTask() {
//                    return new Task<ObservableList<User>>() {
//                        @Override
//                        protected ObservableList<User> call() throws Exception {
//                            // Realiza la carga de datos desde la base de datos aquí
//                            // Obtén la lista de usuarios desde tu UserDao
//                            ObservableList<User> userList = FXCollections.observableArrayList(userDao.getAllUsers());
//                            return userList;
//                        }
//                    };
//                }
//            };
//
//            // Acción cuando el hilo se inicia
//            dataLoadingService.setOnRunning(event -> {
//                System.out.println("El hilo se ha iniciado");
//            });
//
//            // Acción cuando el hilo ha terminado con éxito
//            dataLoadingService.setOnSucceeded(event -> {
//                System.out.println("El hilo ha terminado");
//                // Enlaza los datos a la TableView
//                ObservableList<User> userList = dataLoadingService.getValue();
//                userTable.setItems(userList);
//
//                // Marcar que la carga de datos ha terminado
//                isDataLoading = false;
//            });
//
//            dataLoadingService.start();
//        }


    @FXML
    void handleSearchUser(ActionEvent event) {
        // Obtiene el DNI ingresado en el campo de búsqueda y lo limpia de espacios en blanco.
        String dni = textSearch.getText().trim();

        if (!dni.isEmpty()) {
            // Busca un usuario en la base de datos por su DNI.
            UserDTO filteredUsers = userDAO.getUserByDni(dni);

            if (filteredUsers != null) {
                // Si se encuentra un usuario con el DNI ingresado, lo muestra en la tabla.
                ObservableList<UserDTO> userList = FXCollections.observableArrayList(filteredUsers);
                userTable.setItems(userList);
            } else {
                // Si no se encuentra ningún resultado, se limpia la tabla.
                userTable.getItems().clear();
            }

        }
    }


    @FXML
    void handleLoadDate() {
//        loadUserTableData();
        // Carga los datos en la TableView al inicializar la ventana
//        userTable.getItems().clear();
        dataLoadingUtil.loadUserTableData(userTable);

    }

    // Método para filtrar y mostrar usuarios en la tabla
    private void filterUsers(String searchValue) {
        // Verificar si el campo de búsqueda está vacío
        if (searchValue.isEmpty()) {
            // Si está vacío, cargar todos los usuarios
            userTable.getItems().clear();
            dataLoadingUtil.loadUserTableData(userTable);
        } else {
            // Si no está vacío, buscar y cargar usuarios que coincidan con el texto de búsqueda
            // Busca un usuario en la base de datos por su DNI.
            UserDTO filteredUsers = userDAO.getUserByDni(searchValue);
            if (filteredUsers != null) {
                userTable.refresh();
                // Si se encuentra un usuario con el DNI ingresado, lo muestra en la tabla.
                ObservableList<UserDTO> userList = FXCollections.observableArrayList(filteredUsers);
                userTable.setItems(userList);
            } else {
                // Si no se encuentra ningún resultado, se limpia la tabla.
                userTable.getItems().clear();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        userDAO = new UserDAOHibernate(HibernateUtil.getSessionFactory());
        configureTable();
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        dataLoadingUtil = new UserDataLoadingUtil(userDAO);
        // Carga los datos en la TableView al inicializar la ventana
        dataLoadingUtil.loadUserTableData(userTable);


        userUtil = new UserUtil(textSearch, userTable);
        userUtil.startUserSearch();


//        // Iniciar una tarea en un hilo separado para cargar los datos
//        Thread loadDataThread = new Thread(() -> {
//            loadUserTableData();
//            Thread.currentThread().interrupt(); // Interrumpir este hilo una vez que los datos se han cargado
//        });
//        loadDataThread.setDaemon(true);
//        loadDataThread.start();


    }
}

