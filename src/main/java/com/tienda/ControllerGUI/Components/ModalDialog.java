package com.tienda.ControllerGUI.Components;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class ModalDialog extends StackPane {

    @FXML
    private StackPane stackPane;
    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado

    @FXML
    private VBox vBox;
    @FXML
    private ImageView iconImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label labelContent;
    @FXML
    private HBox hBox;
    @FXML
    private MFXButton exitButton;
    @FXML
    private MFXButton confirmButton;

    public ModalDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setIconImage(Image image) {
        iconImageView.setImage(image);
    }

    public void setMessage(String message) {
        labelContent.setText(message);
    }

    public void setConfirmButtonText(String text) {
        confirmButton.setText(text);
    }

    public void setExitButtonText(String text) {
        exitButton.setText(text);
    }


    public void setExitButtonAction(EventHandler<ActionEvent> action) {
        exitButton.setOnAction(action);
    }

    public void setConfirmButtonAction(EventHandler<ActionEvent> action) {
        confirmButton.setOnAction(action);
    }

    public void configureModal(Image iconImage, String title, String message, String confirmButtonText, String exitButtonText,
                               EventHandler<ActionEvent> confirmButtonAction, EventHandler<ActionEvent> exitButtonAction) {
        setIconImage(iconImage);
        setTitle(title);
        setMessage(message);
        setConfirmButtonText(confirmButtonText);
        setExitButtonText(exitButtonText);
        setConfirmButtonAction(confirmButtonAction);
        setExitButtonAction(exitButtonAction);
    }

    public void configureModal(Image iconImage, String title, String message, String confirmButtonText,
                               EventHandler<ActionEvent> confirmButtonAction) {
        setIconImage(iconImage);
        setTitle(title);
        setMessage(message);
        setConfirmButtonText(confirmButtonText);
        setConfirmButtonAction(confirmButtonAction);
        exitButton.setVisible(false);

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



}





