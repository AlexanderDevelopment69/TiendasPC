package com.tienda.Utils;

import com.jfoenix.controls.JFXComboBox;
import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.CustomerDAO;
import com.tienda.DaoImpl.CustomerDAOHibernate;
import com.tienda.dto.CustomerDTO;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomerUtil {


    private TextField textSearch; // Campo de búsqueda en la interfaz de usuario
    private TableView<CustomerDTO> customerTable; // Tabla para mostrar los resultados de búsqueda
    private TextField validateCustomer;

    private ScheduledExecutorService executor; // Executor para manejar el hilo de búsqueda

    private CustomerDAO customerDAO; // DAO para acceder a los datos de clientes



    /**
     * Constructor de la clase CustomerUtil.
     *
     * @param textSearch   Campo de búsqueda en la interfaz de usuario.
     * @param customerTable Tabla para mostrar los resultados de búsqueda.
     */
    public CustomerUtil(TextField textSearch, TableView<CustomerDTO> customerTable) {
        this.textSearch = textSearch;
        this.customerTable = customerTable;
        customerDAO= new CustomerDAOHibernate(HibernateUtil.getSessionFactory());
    }

    /**
     * Inicia la búsqueda de clientes cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startCustomerSearch() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            handleSearch(value);
        });
    }

    /**
     * Maneja la búsqueda de clientes y actualiza la tabla de resultados.
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
            // Realiza la búsqueda de clientes en un hilo separado
            List<CustomerDTO> filteredCustomers = customerDAO.searchCustomersBySingleCriteria(searchValue);

            // Actualiza la interfaz de usuario con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredCustomers != null) {
                    // Muestra los resultados en la tabla
                    ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList(filteredCustomers);
                    customerTable.setItems(customerList);
                } else {
                    // Si no se encuentran resultados, limpia la tabla
                    customerTable.getItems().clear();
                }
            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }

//    private void filterCustomers(String searchValue) {
//        // Mostrar mensaje cuando el hilo se inicie
//        System.out.println("Hilo de búsqueda iniciado.");
//        // Verificar si el campo de búsqueda está vacío
//        if (searchValue.isEmpty()) {
//            // Si está vacío, cargar todos los productos
//            customerTable.getItems().clear();
//            productDataLoadingUtil.loadcustomerTableData(customerTable);
//        } else {
//            // Si no está vacío, realizar la búsqueda en un hilo separado
//            Task<List<CustomerDTO>> searchTask = new Task<List<CustomerDTO>>() {
//                @Override
//                protected List<CustomerDTO> call() throws Exception {
//                    return customerDAO.searchProductsBySingleCriteria(searchValue);
//                }
//            };
//
//            // Manejar los resultados cuando la búsqueda se complete
//            searchTask.setOnSucceeded(event -> {
//                List<CustomerDTO> filteredCustomers = searchTask.getValue();
//                if (filteredCustomers != null) {
//                    customerTable.refresh();
//                    // Mostrar los resultados en la tabla
//                    ObservableList<CustomerDTO> userList = FXCollections.observableArrayList(filteredCustomers);
//                    customerTable.setItems(userList);
//                } else {
//                    // Si no se encuentra ningún resultado, limpiar la tabla
//                    customerTable.getItems().clear();
//                }
//                // Mostrar mensaje cuando el hilo se detenga
//                System.out.println("Hilo de búsqueda finalizado.");
//            });
//
//            // Iniciar la búsqueda en un hilo separado
//            Thread thread = new Thread(searchTask);
//            thread.setDaemon(true); // Opcionalmente, puedes configurar el hilo como demonio
//            thread.start();
//        }
//    }




    public CustomerUtil(TextField textSearch, TextField validateCustomer ) {
        this.textSearch = textSearch;
        this.validateCustomer =  validateCustomer;
        customerDAO= new CustomerDAOHibernate(HibernateUtil.getSessionFactory());
    }

    /**
     * Inicia la búsqueda de clientes cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startCustomerSearchCustomer() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
//            handleSearchCustomer(value);

        });

    }



    /**
     * Maneja la búsqueda de clientes.
     *
     * @param searchValue Valor de búsqueda ingresado por el usuario.
     */
    public void handleSearchCustomer(String searchValue) {

        if (executor != null && !executor.isTerminated()) {
            // Detiene el hilo de búsqueda anterior si está en ejecución
            System.out.println("Deteniendo hilo de búsqueda anterior...");
            executor.shutdownNow();
        }
        // Inicia un nuevo ScheduledExecutorService con 1 hilo
        executor = Executors.newScheduledThreadPool(1);
        System.out.println("Iniciando hilo de búsqueda...");


        executor.schedule(() -> {

            // Realiza la búsqueda de clientes en un hilo separado
            CustomerDTO filteredCustomers = customerDAO.getCustomerByDniOrRuc(searchValue);

            // Actualiza la interfaz de usuario con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredCustomers != null) {

                    if(filteredCustomers.getCustomerDni()==null){
                        validateCustomer.setText(filteredCustomers.getCustomerRuc());
                    }
                    if(filteredCustomers.getCustomerRuc()==null){
                        validateCustomer.setText(filteredCustomers.getCustomerDni());
                    }

                    if(filteredCustomers.getCustomerDni()!=null && filteredCustomers.getCustomerRuc()!=null){
                        validateCustomer.setText(filteredCustomers.getCustomerDni());
                    }

                    CustomerDTO customerDTO= new CustomerDTO();
                    customerDTO.setCustomerDni(filteredCustomers.getCustomerDni());


                }else{
                    validateCustomer.setText("");


                }
            });
            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms

    }

    /**
     * Realiza la búsqueda de clientes de manera asíncrona y retorna un CompletableFuture que contendrá los datos del cliente si se encuentra.
     *
     * @param searchValue Valor de búsqueda ingresado por el usuario.
     * @return Un CompletableFuture que contendrá el objeto CustomerDTO si se encuentra o null si no se encuentra.
     */
    public CompletableFuture<CustomerDTO> handleSearchCustomerAsync(String searchValue) {
        CompletableFuture<CustomerDTO> future = new CompletableFuture<>();

        if (executor != null && !executor.isTerminated()) {
            // Detiene el hilo de búsqueda anterior si está en ejecución
            System.out.println("Deteniendo hilo de búsqueda anterior...");
            executor.shutdownNow();
        }

        // Inicia un nuevo ScheduledExecutorService con 1 hilo
        executor = Executors.newScheduledThreadPool(1);
        System.out.println("Iniciando hilo de búsqueda...");

        executor.schedule(() -> {
            // Realiza la búsqueda de clientes en un hilo separado
            CustomerDTO foundCustomer = customerDAO.getCustomerByDniOrRuc(searchValue);

            // Actualiza la interfaz de usuario con los resultados de la búsqueda
            Platform.runLater(() -> {


            if (foundCustomer != null) {

                if(foundCustomer.getCustomerDni()==null){
                    validateCustomer.setText(foundCustomer.getCustomerRuc());
                }
                if(foundCustomer.getCustomerRuc()==null){
                    validateCustomer.setText(foundCustomer.getCustomerDni());
                }

                if(foundCustomer.getCustomerDni()!=null && foundCustomer.getCustomerRuc()!=null){
                    validateCustomer.setText(foundCustomer.getCustomerDni());
                }

                CustomerDTO customerDTO= new CustomerDTO();
                customerDTO.setCustomerDni(foundCustomer.getCustomerDni());


            }else{
                validateCustomer.setText("");


            }

            });

            // Completa el CompletableFuture con el objeto CustomerDTO encontrado
            future.complete(foundCustomer);
        }, 500, TimeUnit.MILLISECONDS);

        return future; // Devuelve el CompletableFuture
    }


}
