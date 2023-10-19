package com.tienda.Utils;

import com.tienda.Dao.PurchaseDAO;
import com.tienda.dto.PurchaseDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class PurchaseDataLoadingUtil {

    private PurchaseDAO purchaseDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    public PurchaseDataLoadingUtil(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
    }

    public void loadPurchaseTableData(TableView<PurchaseDTO>purchaseTable) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<PurchaseDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<PurchaseDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<PurchaseDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de compras  desde tu purchaseDAO
                        ObservableList<PurchaseDTO> purchaseList = FXCollections.observableArrayList(purchaseDAO.getAllPurchases());
                        return purchaseList;
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
            purchaseTable.refresh();
            // Enlaza los datos a la TableView
            ObservableList<PurchaseDTO> purchaseList = dataLoadingService.getValue();
            purchaseTable.setItems(purchaseList);

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores





}
