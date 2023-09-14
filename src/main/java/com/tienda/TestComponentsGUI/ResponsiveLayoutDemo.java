package com.tienda.TestComponentsGUI;

import com.tienda.Tools.ResponsiveLayoutHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ResponsiveLayoutDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);

        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        TextField passwordField = new TextField();
        Button registerButton = new Button("Registrarse");
        Button cancelButton = new Button("Cancelar");

        root.addRow(0, usernameField, emailField);
        root.addRow(1, passwordField);
        root.addRow(2, registerButton, cancelButton);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Formulario de Registro Responsivo");

        // Llama al método makeResponsive para hacer que los elementos sean responsivos
        ResponsiveLayoutHelper.makeResponsive(scene, usernameField, emailField, passwordField, registerButton, cancelButton);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
