
package com.tienda.TestComponentsGUI;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SidebarMenuExample extends Application {

    private boolean isExpanded = false;
    private VBox sidebar;
    private double expandedWidth = 200;
    private double collapsedWidth = 55;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Crear el contenido central
        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle("-fx-background-color: #EEE;");

        // Crear el Sidebar (VBox)
        sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: #333;");
        sidebar.setMinWidth(collapsedWidth);
        sidebar.setMaxWidth(collapsedWidth);

        // Agregar iconos (Button con texto oculto) al Sidebar
        Button icon1 = new Button("Icono 1");
        icon1.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        Button icon2 = new Button("Icono 2");
        icon2.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        Button icon3 = new Button("Icono 3");
        icon3.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        sidebar.getChildren().addAll(icon1, icon2, icon3);

        // Crear un botón de hamburguesa
        Button hamburgerButton = new Button("\u2630");
        hamburgerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // Configurar el botón de hamburguesa para mostrar/ocultar el Sidebar
        hamburgerButton.setOnAction(e -> {
            if (!isExpanded) {
                expandSidebar();
            } else {
                collapseSidebar();
            }
            isExpanded = !isExpanded;
        });

        // Agregar elementos al Sidebar
        sidebar.getChildren().add(0, hamburgerButton);

        // Colocar el Sidebar y el contenido central en el BorderPane
        root.setLeft(sidebar);
        root.setCenter(centerPane);

        // Crear la escena
        Scene scene = new Scene(root, 800, 600);

        // Configurar el escenario principal
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private void expandSidebar() {
//        // Animación para expandir suavemente el sidebar
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(200),
//                        new KeyValue(sidebar.minWidthProperty(), expandedWidth, Interpolator.EASE_BOTH),
//                        new KeyValue(sidebar.maxWidthProperty(), expandedWidth, Interpolator.EASE_BOTH)
//                )
//        );
//        timeline.play();
//    }
//
//    private void collapseSidebar() {
//        // Animación para contraer suavemente el sidebar
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(200),
//                        new KeyValue(sidebar.minWidthProperty(), collapsedWidth, Interpolator.EASE_BOTH),
//                        new KeyValue(sidebar.maxWidthProperty(), collapsedWidth, Interpolator.EASE_BOTH)
//                )
//        );
//        timeline.play();
//    }

    private void expandSidebar() {
        // Animación para expandir suavemente el sidebar con una duración de 400 ms
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400),
                        new KeyValue(sidebar.minWidthProperty(), expandedWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                )
        );
        timeline.play();
    }

    private void collapseSidebar() {
        // Animación para contraer suavemente el sidebar con una duración de 400 ms
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400),
                        new KeyValue(sidebar.minWidthProperty(), collapsedWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                )
        );
        timeline.play();
    }

}






















//package com.tienda;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class SidebarMenuExample extends Application {
//
//    private boolean isExpanded = false;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        BorderPane root = new BorderPane();
//
//        // Crear el contenido central
//        AnchorPane centerPane = new AnchorPane();
//        centerPane.setStyle("-fx-background-color: #EEE;");
//
//        // Crear el Sidebar (VBox)
//        VBox sidebar = new VBox(10);
//        sidebar.setStyle("-fx-background-color: #333;");
//        sidebar.setMinWidth(55);
//        sidebar.setMaxWidth(55);
//
//        // Agregar iconos (Button con texto oculto) al Sidebar
//        Button icon1 = new Button("Icono 1");
//        icon1.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
//        Button icon2 = new Button("Icono 2");
//        icon2.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
//        Button icon3 = new Button("Icono 3");
//        icon3.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
//
//        sidebar.getChildren().addAll(icon1, icon2, icon3);
//
//        // Crear un botón de hamburguesa
//        Button hamburgerButton = new Button("\u2630");
//        hamburgerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
//
//        // Configurar el botón de hamburguesa para mostrar/ocultar el Sidebar
//        hamburgerButton.setOnAction(e -> {
//            if (!isExpanded) {
//                sidebar.setMinWidth(200);
//                sidebar.setMaxWidth(200);
//            } else {
//                sidebar.setMinWidth(55);
//                sidebar.setMaxWidth(55);
//            }
//            isExpanded = !isExpanded;
//        });
//
//        // Agregar elementos al Sidebar
//        sidebar.getChildren().add(0, hamburgerButton);
//
//        // Colocar el Sidebar y el contenido central en el BorderPane
//        root.setLeft(sidebar);
//        root.setCenter(centerPane);
//
//        // Crear la escena
//        Scene scene = new Scene(root, 800, 600);
//
//        // Configurar el escenario principal
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}


