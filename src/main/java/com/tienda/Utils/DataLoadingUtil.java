package com.tienda.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import com.tienda.DAO.UserDao;
import com.tienda.Model.User;

public class DataLoadingUtil {

    private UserDao userDao;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public DataLoadingUtil(UserDao userDao) {
        this.userDao = userDao;
    }

    public void loadUserTableData(TableView<User> userTable) {
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


    // Otros métodos de utilidad compartidos entre controladores





}
