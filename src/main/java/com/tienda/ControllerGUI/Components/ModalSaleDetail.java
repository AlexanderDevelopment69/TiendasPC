package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.SaleDAO;
import com.tienda.DaoImpl.PurchaseDAOHibernate;
import com.tienda.DaoImpl.SaleDAOHibernate;
import com.tienda.Utils.PurchaseDetailDataLoadingUtil;
import com.tienda.Utils.SaleDetailDataLoadingUtil;
import com.tienda.dto.SaleDetailDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalSaleDetail extends StackPane  implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton closeButton;

    @FXML
    private TableColumn<SaleDetailDTO,String> colBrand;

    @FXML
    private TableColumn<SaleDetailDTO,Long> colCode;

    @FXML
    private TableColumn<SaleDetailDTO,String> colDescription;

    @FXML
    private TableColumn<SaleDetailDTO,String> colDiscount;

    @FXML
    private TableColumn<SaleDetailDTO, BigDecimal> colPrice;

    @FXML
    private TableColumn<SaleDetailDTO,String> colProductName;

    @FXML
    private TableColumn<SaleDetailDTO,String> colQuantity;

    @FXML
    private TableColumn<SaleDetailDTO,String> colSubTotal;

    @FXML
    private MFXLegacyTableView<SaleDetailDTO> saleDetailTable;

    @FXML
    private StackPane stackPane;

    @FXML
    private MFXTextField txtCodeSale;

    @FXML
    private MFXTextField txtTotalSale;

    SaleDAO saleDAO;

   SaleDetailDataLoadingUtil saleDetailDataLoadingUtil;


    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado



    public ModalSaleDetail() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalSaleDetail.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }



    public void setCancelButtonAction(EventHandler<ActionEvent> action) {
        closeButton.setOnAction(action);
    }


    public void configureModal(EventHandler<ActionEvent> cancelPurchase) {

        setCancelButtonAction(cancelPurchase);
    }

    public void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        saleDetailTable.setOnMouseExited(event -> {
            // Deseleccionar la fila activa
            saleDetailTable.getSelectionModel().clearSelection();
        });

        // Configura las columnas de la tabla una vez en initialize()
        // Configura la columna "colCode" para mostrar el ID del producto.
        colCode.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductId()));
        // Configura la columna "colProductName" para mostrar el nombre del producto.
        colProductName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductName()));
        // Configura la columna "colBrand" para mostrar la marca del producto.
        colBrand.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductBrand()));
        // Configura la columna "colDescription" para mostrar la descripción del producto.
        colDescription.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getProductDescription()));
        // Configura la columna "colPrice" para mostrar el precio del producto.
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getUnitPrice()));

        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountPerProduct"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotalPerProduct"));

    }


    public void showModal(StackPane parentStackPane) {
        // Añade el fondo oscuro al StackPane
        backdrop = createBackdrop(parentStackPane);
        parentStackPane.getChildren().add(backdrop);
        // Agrega un evento para cerrar el modal haciendo clic fuera de él
        backdrop.setOnMouseClicked(event -> close());


        // Calcula la posición para que el modal aparezca en el centro del StackPane
        double centerX = parentStackPane.getWidth() / 2;
        double centerY = parentStackPane.getHeight() / 2;

        // Establece la posición inicial del modal
        setLayoutX(centerX - (getPrefWidth() / 2));
        setLayoutY(centerY - (getPrefHeight() / 2));


        // Añade el modal al StackPane
        parentStackPane.getChildren().add(this);

        // Aplica la animación de zoom y opacidad con interpoladores personalizados
        setScaleX(0.7);
        setScaleY(0.7);
        setOpacity(0);

        // Interpolador personalizado para la escala
        Interpolator scaleInterpolator = Interpolator.SPLINE(0.4, 0.1, 0.2, 1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(scaleInterpolator);

        // Interpolador personalizado para la opacidad
        Interpolator opacityInterpolator = Interpolator.SPLINE(0.1, 0.1, 0.25, 1);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(1);
        fadeTransition.setInterpolator(opacityInterpolator);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();


    }

    private StackPane createBackdrop(StackPane parentStackPane) {
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Color de fondo oscuro
        backdrop.setPrefSize(parentStackPane.getWidth(), parentStackPane.getHeight());
        return backdrop;
    }

    public void close() {
        if (isClosed) {
            return; // Evitar cierre múltiple
        }

        isClosed = true; // Establecer la bandera de cierre

        // Aplica la animación inversa al cerrar el modal
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(0.1);
        scaleTransition.setToY(0.1);
        scaleTransition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(0);
        fadeTransition.setInterpolator(Interpolator.SPLINE(0.1, 0.1, 0.25, 1));

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> {
            StackPane parentStackPane = (StackPane) getParent();
            parentStackPane.getChildren().remove(backdrop);
            parentStackPane.getChildren().remove(this); // Remueve el modal
        });
        parallelTransition.play();
    }



    public void loadingDataToTable(long saleId){

        saleDetailDataLoadingUtil.loadSaleDetailTableData(saleDetailTable,saleId);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();

        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        saleDAO = new SaleDAOHibernate(HibernateUtil.getSessionFactory());
        saleDetailDataLoadingUtil= new SaleDetailDataLoadingUtil(saleDAO,txtCodeSale,txtTotalSale);


    }
}
