package com.tienda.Utils;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.ProductDAO;
import com.tienda.Dao.SaleDAO;
import com.tienda.DaoImpl.ProductDAOHibernate;
import com.tienda.Model.Sale;
import com.tienda.dto.ProductDTO;
import com.tienda.dto.SaleDTO;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaleUtil {

    private TextField textSearch; // Campo de búsqueda en la interfaz de usuario
    private TableView<SaleDTO> saleTable; // Tabla para mostrar los resultados de búsqueda

    private MFXLegacyComboBox<String> cbProductName; // Campo para mostrar el nombre del Supplier encontrado
    private ScheduledExecutorService executor; // Executor para manejar el hilo de búsqueda

    private ProductDAO productDAO; // DAO para acceder a los datos de productos

    private SaleDAO saleDAO;

    /**
     * Constructor de la clase ProductUtil.
     *
     * @param textSearch   Campo de búsqueda en la interfaz de usuario.
     * @param saleTable Tabla para mostrar los resultados de búsqueda.
     */
    public SaleUtil(TextField textSearch, TableView<SaleDTO> saleTable) {
        this.textSearch = textSearch;
        this.saleTable = saleTable;
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
    }
    /**
     * Constructor de la clase ProductUtil para búsqueda por productos por proveedor
     *
     * @param  cbProductName Campo de texto para mostrar el nombre del producto encontrado.
     */
    public SaleUtil(MFXLegacyComboBox<String> cbProductName) {
        this. cbProductName=  cbProductName;
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
    }







    // Método para cargar los productos  de los proveedores en el ComboBox
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






}
