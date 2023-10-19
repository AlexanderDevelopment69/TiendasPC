package com.tienda.Utils;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.UserDAO;
import com.tienda.DaoImpl.UserDAOHibernate;
import com.tienda.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserUtil {

    private TextField textSearch; // Campo de búsqueda en la interfaz de usuario
    private TableView<UserDTO> userTable; // Tabla para mostrar los resultados de búsqueda
    private ScheduledExecutorService executor; // Executor para manejar el hilo de búsqueda

    private UserDAO userDAO; // DAO para acceder a los datos de usuarios

    /**
     * Constructor de la clase UserUtil.
     *
     * @param textSearch   Campo de búsqueda en la interfaz de usuario.
     * @param userTable Tabla para mostrar los resultados de búsqueda.
     */
    public UserUtil(TextField textSearch, TableView<UserDTO> userTable) {
        this.textSearch = textSearch;
        this.userTable = userTable;
        userDAO= new UserDAOHibernate(HibernateUtil.getSessionFactory());
    }

    /**
     * Inicia la búsqueda de usuarios cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startUserSearch() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            handleSearch(value);
        });
    }

    /**
     * Maneja la búsqueda de usuarios y actualiza la tabla de resultados.
     *
     * @param searchValue Valor de búsqueda ingresado por el usuario.
     */
    private void handleSearch(String searchValue) {
        if (executor != null && !executor.isTerminated()) {
            // Detiene el hilo de búsqueda anterior si está en ejecución
            System.out.println("Deteniendo hilo de búsqueda anterior...");
            executor.shutdownNow();
        }
        // Inicia un nuevo ScheduledExecutorService con 1 hilo
        executor = Executors.newScheduledThreadPool(1);
        System.out.println("Iniciando hilo de búsqueda...");

        executor.schedule(() -> {
            // Realiza la búsqueda de usuarios en un hilo separado
            List<UserDTO> filteredUsers = userDAO.searchUsersBySingleCriteria(searchValue);

            // Actualiza la interfaz de usuario con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredUsers != null) {
                    // Muestra los resultados en la tabla
                    ObservableList<UserDTO> userList = FXCollections.observableArrayList(filteredUsers);
                    userTable.setItems(userList);
                } else {
                    // Si no se encuentran resultados, limpia la tabla
                    userTable.getItems().clear();
                }
            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }


}
