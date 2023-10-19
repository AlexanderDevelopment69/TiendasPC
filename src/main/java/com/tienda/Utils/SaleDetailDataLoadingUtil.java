package com.tienda.Utils;

import com.tienda.Dao.SaleDAO;
import com.tienda.dto.SaleDetailDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class SaleDetailDataLoadingUtil {

    private SaleDAO saleDAO;
    private boolean isDataLoading = false; // Controla si la carga de datos está en curso

    TextField totalSumText;
    TextField txtCodeSale;

    public SaleDetailDataLoadingUtil(SaleDAO saleDAO,TextField txtCodeSale, TextField totalSumText) {
        this.saleDAO = saleDAO;
        this.totalSumText=totalSumText;
        this.txtCodeSale=txtCodeSale;
    }

    public void loadSaleDetailTableData(TableView<SaleDetailDTO>saleTable, long saleId) {
        // Verificar si la carga de datos ya está en curso
        if (isDataLoading) {
            // El hilo ya se ha iniciado, no hacer nada adicional
            return;
        }

        isDataLoading = true; // Marcar que la carga de datos está en curso

        Service<ObservableList<SaleDetailDTO>> dataLoadingService = new Service<>() {
            @Override
            protected Task<ObservableList<SaleDetailDTO>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<SaleDetailDTO> call() {
//                         Realiza la carga de datos desde la base de datos aquí
//                         Obtén la lista de compras  desde tu saleDAO
                        ObservableList<SaleDetailDTO> saleDetailList = FXCollections.observableArrayList(saleDAO.getSaleDetailsBySaleId(saleId));
                        return saleDetailList;
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
            ObservableList<SaleDetailDTO> saleList = dataLoadingService.getValue();
            saleTable.setItems(saleList);

            totalSumText.setText(getSumSubtotal(saleTable));

            txtCodeSale.setText(String.valueOf(saleId));

            // Marcar que la carga de datos ha terminado
            isDataLoading = false;
        });

        dataLoadingService.start();
    }


    // Otros métodos de utilidad compartidos entre controladores

    public String getSumSubtotal(TableView<SaleDetailDTO> saleDetailTable) {
        // Crear una StringExpression que se actualiza automáticamente
        StringExpression totalSumBinding = Bindings.createStringBinding(() -> {
            BigDecimal totalSum = BigDecimal.ZERO; // Inicializa con 0.00

            ObservableList<SaleDetailDTO> items = saleDetailTable.getItems();
            for (SaleDetailDTO item : items) {
                totalSum = totalSum.add(item.getSubtotalPerProduct());
            }
            return String.valueOf(totalSum);
        }, saleDetailTable.getItems());

        // Obtén el valor actual calculado y devuélvelo como una cadena
        String totalSumText = totalSumBinding.get();

        return totalSumText;
    }




}
