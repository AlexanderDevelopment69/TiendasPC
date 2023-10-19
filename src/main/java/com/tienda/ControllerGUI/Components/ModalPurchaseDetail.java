package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.User.UserSession;
import com.tienda.Dao.PurchaseDAO;
import com.tienda.Dao.PurchaseDetailDAO;
import com.tienda.Dao.SupplierDAO;
import com.tienda.DaoImpl.PurchaseDAOHibernate;
import com.tienda.DaoImpl.PurchaseDetailDAOHibernate;
import com.tienda.DaoImpl.SupplierDAOHibernate;
import com.tienda.Model.Supplier;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.Utils.ProductUtil;
import com.tienda.Utils.PurchaseDetailDataLoadingUtil;
import com.tienda.Utils.SupplierUtil;
import com.tienda.dto.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ModalPurchaseDetail extends StackPane implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton closeButton;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colProductName;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colBrand;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colCode;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colDescription;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colCost;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colQuantity;

    @FXML
    private TableColumn<PurchaseDetailDTO, String> colSubTotal;

    @FXML
    private MFXLegacyTableView<PurchaseDetailDTO> purchaseDetailTable;

    @FXML
    private MFXTextField txtTotalPurchase;
    @FXML
    private MFXTextField txtCodePurchase;

    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado


    PurchaseDAO purchaseDAO;

    PurchaseDetailDataLoadingUtil purchaseDetailDataLoadingUtil;




    public ModalPurchaseDetail() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalPurchaseDetail.fxml"));
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
        purchaseDetailTable.setOnMouseExited(event -> {
            // Deseleccionar la fila activa
            purchaseDetailTable.getSelectionModel().clearSelection();
        });

        // Configura las columnas de la tabla una vez en initialize()
        colCode.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("UnitPriceAtPurchase"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityPurchased"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));





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

    public void loadingDataToTable(long purchaseId){

        purchaseDetailDataLoadingUtil.loadPurchaseTableData(purchaseDetailTable,purchaseId);


    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        purchaseDAO = new PurchaseDAOHibernate(HibernateUtil.getSessionFactory());
        purchaseDetailDataLoadingUtil= new PurchaseDetailDataLoadingUtil(purchaseDAO,txtCodePurchase,txtTotalPurchase);
        configureTable();





    }

}
