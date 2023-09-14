package com.tienda.TestComponentsGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UsePrefSizeExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ejemplo de USE_PREF_SIZE");

        // Crea un Button
        Button button = new Button("Botón");

        // Establece el ancho preferido del botón
        button.setPrefWidth(100);


        // Crea un contenedor y agrega el Button
        HBox hbox = new HBox(button);

        // Crea una escena y muestra el contenedor en la ventana
        Scene scene = new Scene(hbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
