package com.tienda.ControllerGUI.User;

import com.tienda.Configs.HibernateUtil;
import com.tienda.DAO.UserDao;
import com.tienda.DAO.UserDaoHibernate;
import com.tienda.Model.User;
import com.tienda.Utils.DataLoadingUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserManagementController implements Initializable {

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private MFXTextField txtfSearchUser;

    @FXML
    private MFXLegacyTableView<User> userTable;

    @FXML
    private TableColumn<?, ?> colCodigo;

    @FXML
    private TableColumn<?, ?> colDni;

    @FXML
    private TableColumn<?, ?> colApellidos;

    @FXML
    private TableColumn<?, ?> colNombres;

    @FXML
    private TableColumn<?, ?> colEmail;

    private UserDao userDao;

    private DataLoadingUtil dataLoadingUtil;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso


    private void configureTable() {
        // Configura las celdas de la tablaalex
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    public void loadUserTableData() {

        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

            Service<ObservableList<User>> dataLoadingService = new Service<>() {
                @Override
                protected Task<ObservableList<User>> createTask() {
                    return new Task<ObservableList<User>>() {
                        @Override
                        protected ObservableList<User> call() throws Exception {
                            // Realiza la carga de datos desde la base de datos aquí
                            // Obtén la lista de usuarios desde tu UserDao
                            ObservableList<User> userList = FXCollections.observableArrayList(userDao.getAllUsers());
                            return userList;
                        }
                    };
                }
            };

            // Acción cuando el hilo se inicia
            dataLoadingService.setOnRunning(event -> {
                System.out.println("El hilo se ha iniciado");
            });

            // Acción cuando el hilo ha terminado con éxito
            dataLoadingService.setOnSucceeded(event -> {
                System.out.println("El hilo ha terminado");
                // Enlaza los datos a la TableView
                ObservableList<User> userList = dataLoadingService.getValue();
                userTable.setItems(userList);

                // Marcar que la carga de datos ha terminado
                isDataLoading = false;
            });

            dataLoadingService.start();
        }


    @FXML
    void handleLoadDate(ActionEvent event) {
//        loadUserTableData();
        // Carga los datos en la TableView al inicializar la ventana
        dataLoadingUtil.loadUserTableData(userTable);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        userDao = new UserDaoHibernate(HibernateUtil.getSessionFactory());
        configureTable();
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        dataLoadingUtil = new DataLoadingUtil(userDao);
        // Carga los datos en la TableView al inicializar la ventana
        dataLoadingUtil.loadUserTableData(userTable);




//        loadUserTableData();


//        // Iniciar una tarea en un hilo separado para cargar los datos
//        Thread loadDataThread = new Thread(() -> {
//            loadUserTableData();
//            Thread.currentThread().interrupt(); // Interrumpir este hilo una vez que los datos se han cargado
//        });
//        loadDataThread.setDaemon(true);
//        loadDataThread.start();



    }
}

