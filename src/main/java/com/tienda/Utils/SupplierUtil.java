package com.tienda.Utils;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.SupplierDAO;
import com.tienda.DaoImpl.SupplierDAOHibernate;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SupplierUtil implements Initializable {

    private TextField textSearch; // Campo de búsqueda en la interfaz de proveedor
    private TableView<SupplierDTO> supplierTable; // Tabla para mostrar los resultados de búsqueda

    private MFXLegacyComboBox<String> cbSupplierName; // Campo para mostrar el nombre del Supplier encontrado

    private ScheduledExecutorService executor; // Executor para manejar el hilo de búsqueda
    private SupplierDAO supplierDAO; // DAO para acceder a los datos de proveedores

    /**
     * Constructor de la clase SupplierUtil.
     *
     * @param textSearch   Campo de búsqueda en la interfaz de proveedor.
     * @param supplierTable Tabla para mostrar los resultados de búsqueda.
     */
    public SupplierUtil(TextField textSearch, TableView<SupplierDTO> supplierTable) {
        this.textSearch = textSearch;
        this.supplierTable = supplierTable;
        supplierDAO= new SupplierDAOHibernate(HibernateUtil.getSessionFactory());
    }

    /**
     * Constructor de la clase SupplierUtil para búsqueda por nombre de proveedor.
     *
     * @param textSearch      Campo de búsqueda en la interfaz de proveedor.
     * @param cbSupplierName  Campo de texto para mostrar el nombre del Supplier encontrado.
     */
    public SupplierUtil(TextField textSearch, MFXLegacyComboBox<String> cbSupplierName) {
        this.textSearch = textSearch;
        this.cbSupplierName = cbSupplierName;
        supplierDAO = new SupplierDAOHibernate(HibernateUtil.getSessionFactory());
    }





    /**
     * Inicia la búsqueda de proveedores cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startSupplierSearch() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            handleSearch(value);
        });
    }


    /**
     * Maneja la búsqueda de proveedores y actualiza la tabla de resultados.
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
            // Realiza la búsqueda de proveedores en un hilo separado
            List<SupplierDTO> filteredSuppliers = supplierDAO.searchSuppliersBySingleCriteria(searchValue);

            // Actualiza la interfaz de proveedor con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredSuppliers != null) {
                    // Muestra los resultados en la tabla
                    ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList(filteredSuppliers);
                    supplierTable.setItems(supplierList);
                } else {
                    // Si no se encuentran resultados, limpia la tabla
                    supplierTable.getItems().clear();
                }
            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }


    /**
     * Inicia la búsqueda de proveedores y establece el nombre del Supplier encontrado cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startSupplierSearchAndSetSupplierName() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            handleSearchAndSetSupplier(value);
        });
    }



    /**
     * Maneja la búsqueda de proveedores y establece el nombre del Supplier encontrado.
     *
     * @param searchValue Valor de búsqueda ingresado por el usuario.
     */
    private void handleSearchAndSetSupplier(String searchValue) {
        if (executor != null && !executor.isTerminated()) {
            // Detiene el hilo de búsqueda anterior si está en ejecución
            System.out.println("Deteniendo hilo de búsqueda anterior...");
            executor.shutdownNow();
        }
        // Inicia un nuevo ScheduledExecutorService con 1 hilo
        executor = Executors.newScheduledThreadPool(1);
        System.out.println("Iniciando hilo de búsqueda...");

        executor.schedule(() -> {
            // Realiza la búsqueda de proveedores en un hilo separado
            SupplierDTO filteredSupplier = supplierDAO.getSupplierBySupplierNameOrRuc(searchValue);

            // Actualiza la interfaz de proveedor con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredSupplier != null) {
                    // Establece el nombre del Supplier encontrado en el campo txtSupplierName
                    cbSupplierName.setValue(filteredSupplier.getSupplierName());
                } else {
                    // Si no se encuentra ningún resultado, limpia el campo txtSupplierName
                    cbSupplierName.setValue(null);
                }
            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }

    // Método para cargar los nombres de los proveedores en el ComboBox
   public void loadSupplierNamesToComboBox() {
        // Obtén la lista de proveedores desde la base de datos
        List<SupplierDTO> suppliers = supplierDAO.getAllSuppliers();

        // Extrae solo los nombres de los proveedores
        List<String> supplierNames = new ArrayList<>();
        for (SupplierDTO supplier : suppliers) {
            supplierNames.add(supplier.getSupplierName());
        }

        // Convierte la lista de nombres en un ObservableList
        ObservableList<String> observableSupplierNames = FXCollections.observableArrayList(supplierNames);

        // Configura el MFXLegacyComboBox con la lista de nombres
        cbSupplierName.setItems(observableSupplierNames);
    }


    // Método para obtener el ID del proveedor seleccionado en el ComboBox
    public Long getSelectedSupplierId() {
        // Obtiene el índice seleccionado en el ComboBox
        int selectedIndex = cbSupplierName.getSelectionModel().getSelectedIndex();

        // Asegúrate de que el índice sea válido
        if (selectedIndex >= 0) {
            // Obtiene la lista de proveedores desde tu base de datos
            List<SupplierDTO> suppliers = supplierDAO.getAllSuppliers();

            // Obtiene el nombre del proveedor seleccionado
            String selectedSupplierName = cbSupplierName.getItems().get(selectedIndex);

            // Busca el proveedor en la lista de proveedores
            for (SupplierDTO supplier : suppliers) {
                if (supplier.getSupplierName().equals(selectedSupplierName)) {
                    // Devuelve el ID del proveedor encontrado
                    return supplier.getSupplierId();
                }
            }
        }

        // Si no se encontró el proveedor seleccionado, puedes devolver un valor por defecto o manejarlo según tus necesidades
        return null;
    }


    // Método para obtener el ID del proveedor por su nombre
    public Long getSupplierIdByName(String supplierName) {
        // Obtiene la lista de proveedores desde tu base de datos
        List<SupplierDTO> suppliers = supplierDAO.getAllSuppliers();

        // Busca el proveedor en la lista de proveedores
        for (SupplierDTO supplier : suppliers) {
            if (supplier.getSupplierName().equals(supplierName)) {
                // Devuelve el ID del proveedor encontrado
                return supplier.getSupplierId();
            }
        }

        // Si no se encuentra el proveedor, devuelve null o maneja según tus necesidades
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
