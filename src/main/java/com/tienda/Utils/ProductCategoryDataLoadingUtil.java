package com.tienda.Utils;

import com.tienda.Dao.ProductCategoryDAO;
import com.tienda.dto.ProductCategoryDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class ProductCategoryDataLoadingUtil {

    private ProductCategoryDAO productCategoryDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public ProductCategoryDataLoadingUtil(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public void loadProductTableData(TableView<ProductCategoryDTO>productCategoryTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<ProductCategoryDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<ProductCategoryDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<ProductCategoryDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de categorias de productos  desde tu productCategoryDAO
                        ObservableList<ProductCategoryDTO> productList = FXCollections.observableArrayList(productCategoryDAO.getAllCategories());
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
           productCategoryTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<ProductCategoryDTO> productList = dataLoadingService.getValue();
           productCategoryTable.setItems(productList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
