package com.tienda.Utils;

import com.tienda.Dao.SaleDAO;
import com.tienda.dto.SaleDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class SaleDataLoadingUtil {

    private SaleDAO saleDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public SaleDataLoadingUtil(SaleDAO saleDAO) {
        this.saleDAO = saleDAO;
    }

    public void loadSaleTableData(TableView<SaleDTO>saleTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<SaleDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<SaleDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<SaleDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de compras  desde tu saleDAO
                        ObservableList<SaleDTO> saleList = FXCollections.observableArrayList(saleDAO.getAllSales());
                        return saleList;
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
            saleTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<SaleDTO> saleList = dataLoadingService.getValue();
            saleTable.setItems(saleList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
