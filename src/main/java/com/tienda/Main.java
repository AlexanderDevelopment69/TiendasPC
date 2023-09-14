package com.tienda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {


//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/tienda/User/Login.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 550);
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setTitle("Inicio de sesion");
//        stage.setScene(scene);
//        stage.show();


    // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/tienda/login/Login.fxml"));
        Parent root = fxmlLoader.load();
        // Configurar la escena y la ventana (Stage)
        Scene scene = new Scene(root, 600, 550);
        // Establecer tamaño mínimo
        stage.setMinWidth(600);
        stage.setMinHeight(550);
        // Establecer tamaño mínimo
        stage.setWidth(600);
        stage.setHeight(550);

        // Configura el estilo de ventana sin decoración
        stage.initStyle(StageStyle.UNDECORATED);
        // Establece el título como "Inicio de sesión".
        stage.setTitle("Inicio de sesion");
        // Establece la escena en el stage (ventana principal) de la aplicación.
        stage.setScene(scene);

//      Configurar manejadores de redimensionamiento y movimiento (si es necesario)
//        ResizeHelper.setResizeHandlers(stage, root);
//        ResizeHelper.setMoveHandlers(stage);

        System.out.println("Stage: "+ stage.getWidth() +", "+stage.getHeight());
        System.out.println("Scene: "+ scene.getWidth() +", "+scene.getHeight());


        // Muestra la ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}