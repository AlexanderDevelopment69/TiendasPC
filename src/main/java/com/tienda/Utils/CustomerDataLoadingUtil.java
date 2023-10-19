package com.tienda.Utils;

import com.tienda.Dao.CustomerDAO;
import com.tienda.dto.CustomerDTO;
import com.tienda.dto.CustomerDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class CustomerDataLoadingUtil {

    private CustomerDAO customerDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public CustomerDataLoadingUtil(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void loadCustomerTableData(TableView<CustomerDTO>customerTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<CustomerDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<CustomerDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<CustomerDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de clientes  desde tu CustomerDAO
                        ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList(customerDAO.getAllCustomers());
                        return customerList;
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
            customerTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<CustomerDTO> productList = dataLoadingService.getValue();
            customerTable.setItems(productList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
