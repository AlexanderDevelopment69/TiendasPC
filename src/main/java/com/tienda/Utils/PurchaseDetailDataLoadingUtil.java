package com.tienda.Utils;

import com.tienda.Dao.PurchaseDAO;
import com.tienda.dto.PurchaseDetailDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class PurchaseDetailDataLoadingUtil {

    private PurchaseDAO purchaseDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    TextField totalSumText;
    TextField txtCodePurchase;

    public PurchaseDetailDataLoadingUtil(PurchaseDAO purchaseDAO,TextField txtCodePurchase, TextField totalSumText) {
        this.purchaseDAO = purchaseDAO;
        this.totalSumText=totalSumText;
        this.txtCodePurchase=txtCodePurchase;
    }

    public void loadPurchaseTableData(TableView<PurchaseDetailDTO>purchaseTable,long purchaseId) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<PurchaseDetailDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<PurchaseDetailDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<PurchaseDetailDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de compras  desde tu purchaseDAO
                        ObservableList<PurchaseDetailDTO> purchaseDetailList = FXCollections.observableArrayList(purchaseDAO.getPurchaseDetailsByPurchaseId(purchaseId));
                        return purchaseDetailList;
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
            ObservableList<PurchaseDetailDTO> purchaseList = dataLoadingService.getValue();
            purchaseTable.setItems(purchaseList);

            totalSumText.setText(getSumSubtotal(purchaseTable));


            txtCodePurchase.setText(String.valueOf(purchaseId));

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores

    public String getSumSubtotal(TableView<PurchaseDetailDTO> purchaseDetailTable) {
        // Crear una StringExpression que se actualiza automáticamente
        StringExpression totalSumBinding = Bindings.createStringBinding(() -> {
            BigDecimal totalSum = BigDecimal.ZERO; // Inicializa con 0.00

            ObservableList<PurchaseDetailDTO> items = purchaseDetailTable.getItems();
            for (PurchaseDetailDTO item : items) {
                totalSum = totalSum.add(item.getSubtotal());
            }
            return String.valueOf(totalSum);
        }, purchaseDetailTable.getItems());

        // Obtén el valor actual calculado y devuélvelo como una cadena
            String totalSumText = totalSumBinding.get();

        return totalSumText;
    }




}
