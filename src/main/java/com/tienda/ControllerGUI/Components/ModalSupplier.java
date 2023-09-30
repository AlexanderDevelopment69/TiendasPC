package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.SupplierDAO;
import com.tienda.Dao.SupplierDAOHibernate;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class ModalSupplier extends StackPane implements Initializable {

    @FXML
    private Label labelDescriptionModal;

    @FXML
    private Label labelTitleModal;
    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXButton confirmButton;

    @FXML
    private StackPane stackPane;

    @FXML
    private MFXTextField txtSupplierAddress;

    @FXML
    private MFXTextField txtSupplierEmail;

    @FXML
    private MFXTextField txtSupplierName;

    @FXML
    private MFXTextField txtSupplierPhoneNumber;

    @FXML
    private MFXTextField txtSupplierRuc;


    @FXML
    private Label labelValidateEmail;

    @FXML
    private Label labelValidatePhoneNumber;

    @FXML
    private Label labelValidateRUC;

    TextFieldValidator rucValidator;
    TextFieldValidator emailValidator;
    TextFieldValidator phoneNumberValidator;
    SupplierDAO supplierDAO;


    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado

    public ModalSupplier() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalSupplier.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public void setConfirmButtonText(String text) {
        confirmButton.setText(text);
    }



    public String getTxtSupplierAddress() {
        return  txtSupplierAddress.getText();
    }

    public String getTxtSupplierEmail() {
        return  txtSupplierEmail.getText();
    }

    public String getTxtSupplierPhoneNumber() {
        return  txtSupplierPhoneNumber.getText();
    }

    public String getTxtSupplierRuc() {
        return  txtSupplierRuc.getText();
    }

    public String getTxtSupplierName() {
        return  txtSupplierName.getText();
    }


    public void setTxtSupplierAddress(String address) {
        txtSupplierAddress.setText(address);
    }

    public void setTxtSupplierEmail(String email) {
        txtSupplierEmail.setText(email);
    }

    public void setTxtSupplierPhoneNumber(String phoneNumber) {
        txtSupplierPhoneNumber.setText(phoneNumber);
    }

    public void setTxtSupplierRuc(String ruc) {
        txtSupplierRuc.setText(ruc);
    }

    public void setTxtSupplierName(String supplierName) {
       txtSupplierName.setText(supplierName);
    }


    public void setCancelButtonAction(EventHandler<ActionEvent> action) {
        cancelButton.setOnAction(action);
    }

    public void setConfirmButtonAction(EventHandler<ActionEvent> action) {
        confirmButton.setOnAction(action);
    }

    public void configureModal(
            EventHandler<ActionEvent> confirmButtonAction, EventHandler<ActionEvent> cancelButton) {

        setConfirmButtonAction(confirmButtonAction);
        setCancelButtonAction(cancelButton);
    }


    public void configureModal(String titleModal,String descriptionModal,String confirmButton,
            EventHandler<ActionEvent> confirmButtonAction, EventHandler<ActionEvent> cancelButton) {
        labelTitleModal.setText(titleModal);
        labelDescriptionModal.setText(descriptionModal);
        setConfirmButtonText(confirmButton);
        setConfirmButtonAction(confirmButtonAction);
        setCancelButtonAction(cancelButton);
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



    public boolean validateTextFieldsSupplier(List<String> errorMessages) {
        boolean validationSuccessful = true;

        // Validar campos vacíos
        if (txtSupplierRuc.getText().isEmpty() || txtSupplierEmail.getText().isEmpty() || txtSupplierPhoneNumber.getText().isEmpty() || txtSupplierName.getText().isEmpty() || txtSupplierAddress.getText().isEmpty()) {
            errorMessages.add("Por favor, complete todos los campos.");
            validationSuccessful = false;
        }

        if (validationSuccessful) {
            if (!rucValidator.validateRUC(txtSupplierRuc.getText())) {
                errorMessages.add("El RUC no cumple con los requisitos.\n");
                validationSuccessful = false;
            }

            if (!emailValidator.validateEmail(txtSupplierEmail.getText())) {
                errorMessages.add("El email no cumple con los requisitos.\n");
                validationSuccessful = false;
            }

            if (!phoneNumberValidator.validatePhoneNumber(txtSupplierPhoneNumber.getText())) {
                errorMessages.add("El numero no cumple con los requisitos.\n");
                validationSuccessful = false;
            }
        }

        return validationSuccessful;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        supplierDAO = new SupplierDAOHibernate(HibernateUtil.getSessionFactory());


        rucValidator= new TextFieldValidator(txtSupplierRuc, labelValidateRUC, TextFieldValidator.ValidationCriteria.RUC);
        emailValidator = new TextFieldValidator(txtSupplierEmail, labelValidateEmail, TextFieldValidator.ValidationCriteria.EMAIL);
        phoneNumberValidator = new TextFieldValidator(txtSupplierPhoneNumber, labelValidatePhoneNumber, TextFieldValidator.ValidationCriteria.PHONE_NUMBER);
    }
}
