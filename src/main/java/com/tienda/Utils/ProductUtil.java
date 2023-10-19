package com.tienda.Utils;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.ProductDAO;
import com.tienda.DaoImpl.ProductDAOHibernate;
import com.tienda.DaoImpl.SupplierDAOHibernate;
import com.tienda.dto.ProductDTO;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProductUtil {

    private TextField textSearch; // Campo de búsqueda en la interfaz de usuario
    private TableView<ProductDTO> productTable; // Tabla para mostrar los resultados de búsqueda

    private MFXLegacyComboBox<String> cbProductName; // Campo para mostrar el nombre del Supplier encontrado
    private ScheduledExecutorService executor; // Executor para manejar el hilo de búsqueda

    private ProductDAO productDAO; // DAO para acceder a los datos de productos

    /**
     * Constructor de la clase ProductUtil.
     *
     * @param textSearch   Campo de búsqueda en la interfaz de usuario.
     * @param productTable Tabla para mostrar los resultados de búsqueda.
     */
    public ProductUtil(TextField textSearch, TableView<ProductDTO> productTable) {
        this.textSearch = textSearch;
        this.productTable = productTable;
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
    }
    /**
     * Constructor de la clase ProductUtil para búsqueda por productos por proveedor
     *
     * @param  cbProductName Campo de texto para mostrar el nombre del producto encontrado.
     */
    public ProductUtil(MFXLegacyComboBox<String> cbProductName) {
        this. cbProductName=  cbProductName;
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
    }


    /**
     * Constructor de la clase ProductUtil para búsqueda por productos por nombre,categoria,id producto
     *
     * @param textSearch      Campo de búsqueda en la interfaz de proveedor.
     *@param  cbProductName Campo de texto para mostrar el nombre del producto encontrado.
     */
    public ProductUtil(TextField textSearch, MFXLegacyComboBox<String> cbProductName) {
        this.textSearch = textSearch;
        this.cbProductName = cbProductName;
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
    }





    /**
     * Inicia la búsqueda de productos cuando se realiza un cambio en el campo de búsqueda.
     */
    public void startProductSearch() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            handleSearch(value);
        });
    }

    /**
     * Maneja la búsqueda de productos y actualiza la tabla de resultados.
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
            // Realiza la búsqueda de productos en un hilo separado
            List<ProductDTO> filteredProducts = productDAO.searchProductsBySingleCriteria(searchValue);

            // Actualiza la interfaz de usuario con los resultados de la búsqueda
            Platform.runLater(() -> {
                if (filteredProducts != null) {
                    // Muestra los resultados en la tabla
                    ObservableList<ProductDTO> productList = FXCollections.observableArrayList(filteredProducts);
                    productTable.setItems(productList);
                } else {
                    // Si no se encuentran resultados, limpia la tabla
                    productTable.getItems().clear();
                }
            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }

//    private void filterProducts(String searchValue) {
//        // Mostrar mensaje cuando el hilo se inicie
//        System.out.println("Hilo de búsqueda iniciado.");
//        // Verificar si el campo de búsqueda está vacío
//        if (searchValue.isEmpty()) {
//            // Si está vacío, cargar todos los productos
//            productTable.getItems().clear();
//            productDataLoadingUtil.loadProductTableData(productTable);
//        } else {
//            // Si no está vacío, realizar la búsqueda en un hilo separado
//            Task<List<ProductDTO>> searchTask = new Task<List<ProductDTO>>() {
//                @Override
//                protected List<ProductDTO> call() throws Exception {
//                    return productDAO.searchProductsBySingleCriteria(searchValue);
//                }
//            };
//
//            // Manejar los resultados cuando la búsqueda se complete
//            searchTask.setOnSucceeded(event -> {
//                List<ProductDTO> filteredUsers = searchTask.getValue();
//                if (filteredUsers != null) {
//                    productTable.refresh();
//                    // Mostrar los resultados en la tabla
//                    ObservableList<ProductDTO> userList = FXCollections.observableArrayList(filteredUsers);
//                    productTable.setItems(userList);
//                } else {
//                    // Si no se encuentra ningún resultado, limpiar la tabla
//                    productTable.getItems().clear();
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


    // Método para cargar los nombres de los productos  por el id del proveedor  en el ComboBox
    public void loadProductNamesToComboBox(Long idSupplier) {
        // Obtén la lista de proveedores desde la base de datos
        List<ProductDTO> products = productDAO.getProductsBySupplier(idSupplier);

        // Extrae solo los nombres de los productos
        List<String> productNames = new ArrayList<>();
        for (ProductDTO product : products) {
            productNames .add(product.getProductName());
        }

        // Convierte la lista de nombres en un ObservableList
        ObservableList<String> observableSupplierNames = FXCollections.observableArrayList(productNames);

        // Configura el MFXLegacyComboBox con la lista de nombres
        cbProductName.setItems(observableSupplierNames);
    }


    // Método para obtener los datos completos del producto seleccionado a partir de su nombre
    public ProductDTO getProductByName(String productName) {
        // Obtén la lista de productos desde la base de datos
        List<ProductDTO> products = productDAO.getAllProducts();

        // Busca el producto en la lista de productos
        for (ProductDTO product : products) {
            if (product.getProductName().equals(productName)) {
                // Devuelve el objeto ProductDTO correspondiente al producto seleccionado
                return product;
            }
        }
        // Si no se encuentra el producto, puedes devolver null o manejarlo según tus necesidades
        return null;
    }




    private boolean loadingProducts = false; // Variable para rastrear si la carga está en curso

    public void loadProductNamesToComboBox() {
        if (loadingProducts) {
            // La carga ya está en curso, no inicies nuevamente
            return;
        }

        loadingProducts = true; // Marcar que la carga está en curso

        Task<ObservableList<String>> loadProductsTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() {
                // Muestra un mensaje cuando el hilo inicia
                Platform.runLater(() -> System.out.println("Cargando productos en un hilo separado..."));

                // Obtén la lista de productos desde la base de datos (simulado aquí)
                List<ProductDTO> products = productDAO.getAllProducts();
                ObservableList<String> productNames = FXCollections.observableArrayList();

                for (ProductDTO product : products) {
                    productNames.add(product.getProductName());
                }

                // Muestra un mensaje cuando el hilo termina
                Platform.runLater(() -> System.out.println("Carga de productos completada."));

                return productNames;
            }
        };

        loadProductsTask.setOnSucceeded(event -> {
            // Cuando el Task haya terminado, actualiza el ComboBox en el hilo de la interfaz de usuario
            cbProductName.setItems(loadProductsTask.getValue());
            loadingProducts = false; // Marcar que la carga ha terminado
        });

        new Thread(loadProductsTask).start(); // Inicia el Task en un hilo separado
    }







    public void startProductSearchAndSetProductName() {
        // Agrega un oyente al cambio en el texto de búsqueda
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Elimina espacios en blanco al principio y al final del texto
            String value = newValue.trim();
            if (value.isEmpty()) {
                // Si el valor está vacío, carga todos los productos en el ComboBox
               loadProductNamesToComboBox();
            } else {
                handleSearchAndSetProduct(value);
            }


        });
    }



    /**
     *
     * @param searchValue Valor de búsqueda ingresado por el usuario.
     */
    private void handleSearchAndSetProduct(String searchValue) {
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
            List<ProductDTO> filteredProduct = productDAO.searchProductByProductNameOrCategoryOrId(searchValue);

            // Actualiza la interfaz de proveedor con los resultados de la búsqueda
            Platform.runLater(() -> {

                if (filteredProduct != null) {
                    // Extrae solo los nombres de los productos
                    List<String> productNames = new ArrayList<>();
                    for(ProductDTO product: filteredProduct){
                        productNames .add(product.getProductName());

                    }

                    // Convierte la lista de nombres en un ObservableList
                    ObservableList<String> observableSupplierNames = FXCollections.observableArrayList(productNames);

                    // Configura el MFXLegacyComboBox con la lista de nombres
                    cbProductName.setItems(observableSupplierNames);

                }

            });

            System.out.println("Hilo de búsqueda finalizado.");
        }, 500, TimeUnit.MILLISECONDS); // Programa el hilo de búsqueda con un retraso de 500 ms
    }



}
