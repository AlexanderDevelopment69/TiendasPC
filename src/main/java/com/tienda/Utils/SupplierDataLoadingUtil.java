package com.tienda.Utils;

import com.tienda.Dao.SupplierDAO;
import com.tienda.dto.SupplierDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class SupplierDataLoadingUtil {

    private SupplierDAO supplierDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public SupplierDataLoadingUtil(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    public void loadSupplierTableData(TableView<SupplierDTO> supplierTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<SupplierDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<SupplierDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<SupplierDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de proveedores desde tu supplierDAO
                        ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList(supplierDAO.getAllSuppliers());
                        return supplierList;
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
            supplierTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<SupplierDTO> supplierList = dataLoadingService.getValue();
            supplierTable.setItems(supplierList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
