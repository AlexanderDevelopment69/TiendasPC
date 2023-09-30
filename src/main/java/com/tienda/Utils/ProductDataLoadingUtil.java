package com.tienda.Utils;

import com.tienda.Dao.ProductDAO;
import com.tienda.dto.ProductDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class ProductDataLoadingUtil {

    private ProductDAO productDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public ProductDataLoadingUtil(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void loadProductTableData(TableView<ProductDTO> productTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<ProductDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<ProductDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<ProductDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de productos desde tu productDAO
                        ObservableList<ProductDTO> productList = FXCollections.observableArrayList(productDAO.getAllProducts());
                        return productList;
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
            productTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<ProductDTO> productList = dataLoadingService.getValue();
            productTable.setItems(productList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
