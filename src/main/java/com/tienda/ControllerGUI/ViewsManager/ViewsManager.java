package com.tienda.ControllerGUI.ViewsManager;

import com.tienda.Transitions.CircularRevealTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewsManager implements ViewsInterface {

    private Region mainContainer; // El contenedor donde se mostrarán las vistas.


    // Constructor de ViewsManager.
    // Recibe un contenedor (Parent) donde se mostrarán las vistas.
    public ViewsManager(Region mainContainer) {
        this.mainContainer = mainContainer;


    }


    // Método para cambiar la vista actual en el StackPane
    // Se recibe la ruta del archivo FXML y un título para la ventana
//    private void showView(String fxmlFileName, String title) {
////        closePreviousStageTypeBorderPane(); // Cierra el Stage de un BorderPane;
//
//
//        Stage stage = (Stage) mainContainer.getScene().getWindow();
//        stage.setTitle(title);
//
//        try {
//            // Crear un objeto FXMLLoader y cargar el archivo FXML
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
//            // Crear un nodo Parent llamado "root" y cargar el contenido del archivo FXML en él
//            Parent root = loader.load();
//            // Limpiamos cualquier vista existente en el StackPane y agregamos la nueva vista
//            mainContainer.getChildren().clear();
//            // Reemplazar el contenido actual del AnchorPane
//            mainContainer.getChildren().setAll(root);
//        } catch (IOException e) {
//            // Manejo de excepciones en caso de error al cargar el archivo FXM
//            e.printStackTrace();
//        }
//    }

    // Método genérico para cambiar una vista desde un archivo FXML y mostrarlo
    public void switchView(String fxmlPath, String title) {
//        Stage stage = (Stage) mainContainer.getScene().getWindow();
//        stage.setTitle(title);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Region newView = loader.load();

            newView.prefWidthProperty().bind(mainContainer.widthProperty());
            newView.prefHeightProperty().bind(mainContainer.heightProperty());



            if (mainContainer instanceof StackPane) {
                // Borra cualquier contenido previo del contenedor principal
                ((StackPane) mainContainer).getChildren().clear();
                ((StackPane) mainContainer).getChildren().add(newView);
//                mainContainer.getScene().setRoot(newView);
            }

            if (mainContainer instanceof AnchorPane) {

                ((AnchorPane) mainContainer).getChildren().clear();
                ((AnchorPane) mainContainer).getChildren().add(newView);

            }


            // Reemplaza la raíz de la escena con la nueva vista
//            mainContainer.getScene().setRoot(newView);


//            ((BorderPane) mainContainer).setCenter(null);
//            ((BorderPane) mainContainer).setCenter(newView);


            System.out.println("Main Container");
            System.out.println(mainContainer.getWidth());
            System.out.println(mainContainer.getHeight());

            System.out.println("New View");
            System.out.println(newView.getWidth());
            System.out.println(newView.getHeight());




        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void switchViewWithCircularReveal(String fxmlPath, String title) {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.setTitle(title);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Region newView = loader.load();


//            CircularRevealTransition transition = new CircularRevealTransition(
//                    stage.getWidth() / 2, stage.getHeight() / 2, Math.min(stage.getWidth(), stage.getHeight()));
//
//            // Configura el contenido en CircularRevealTransition
//            transition.setContent(newView);


            double centerX = mainContainer.getBoundsInParent().getWidth() / 2; // Centro en X del nodo
            double centerY = mainContainer.getBoundsInParent().getHeight() / 2; // Centro en Y del nodo
            double radius = Math.sqrt(Math.pow(centerX, 2) + Math.pow(centerY, 2)); // Calcular el radio basado en la diagonal del nodo

            CircularRevealTransition transition = new CircularRevealTransition(centerX, centerY, radius);
            transition.setContent(newView);


            System.out.println("Main Container");
            System.out.println(mainContainer.getWidth());
            System.out.println(mainContainer.getHeight());

            System.out.println("New View");
            System.out.println(newView.getWidth());
            System.out.println(newView.getHeight());


            // Agrega la transición al contenedor principal sin eliminar los nodos anteriores
            if (mainContainer instanceof StackPane) {
                ((StackPane) mainContainer).getChildren().clear();
                ((StackPane) mainContainer).getChildren().add(transition);
//                ((StackPane) mainContainer).getChildren().add(newView);
            }
            if (mainContainer instanceof AnchorPane) {

                ((AnchorPane) mainContainer).getChildren().clear();
                ((AnchorPane) mainContainer).getChildren().add(transition);

            } else if (mainContainer instanceof BorderPane) {
                // Si es un BorderPane, elige la región adecuada (top, center, bottom, left, o right)
                // y agrega la transición allí.
                ((BorderPane) mainContainer).getChildren().clear();
                ((BorderPane) mainContainer).setCenter(transition);
            }

            // Vincula el ancho preferido del nuevo nodo al ancho preferido del contenedor principal
            // Establece el ancho y alto del nuevo nodo según el mainContainer
//            newView.setPrefWidth(mainContainer.getBoundsInLocal().getWidth());
//            newView.setPrefHeight(mainContainer.getBoundsInLocal().getHeight());

            newView.prefWidthProperty().bind(mainContainer.widthProperty());
            newView.prefHeightProperty().bind(mainContainer.heightProperty());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Método para mostrar una vista en una nueva ventana (Stage)
    // Se recibe la ruta del archivo FXML y un título para la ventana
    private void showViewNewStage(String fxmlFileName, String title) {
        closePreviousStage(); // Cierra el Stage anterior si existe
        try {
            // Crear un objeto FXMLLoader y cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));

            // Crear un nodo Parent llamado "root" y cargar el contenido del archivo FXML en él
            Parent root = loader.load();

            // Crear una escena JavaFX utilizando el nodo "root" como contenido principal
            Scene scene = new Scene(root);

            // Crear una nueva ventana (Stage) de JavaFX
            Stage newStage = new Stage();

            // Stage sin barra
//            newStage.initStyle(StageStyle.UNDECORATED);

            // Establecer la escena recién creada como contenido de la ventana (Stage)
            newStage.setScene(scene);

            // Establecer el título de la ventana (Stage)
            newStage.setTitle(title);

            // Configurar manejadores de redimensionamiento y movimiento (si es necesario)
//            ResizeHelper.setResizeHandlers(newStage , root);
//            ResizeHelper.setMoveHandlers(newStage);

            // Mostrar la ventana (Stage) en pantalla
            newStage.show();


        } catch (IOException e) {
            // Manejo de excepciones en caso de error al cargar el archivo FXM
            e.printStackTrace();
        }
    }

    //     Método para cerrar el Stage actual
    public void closePreviousStage() {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.close();

    }


    @Override
    public void showLogin() {
//        switchView("/com/tienda/User/Login.fxml", "Inicio de sesion");
//        showViewNewStage("/com/tienda/User/Login.fxml", "Inicio de sesion");

        if (mainContainer instanceof StackPane) {
            switchView("/com/tienda/login/Login.fxml", "Inicio de sesion");
        } else if (mainContainer instanceof AnchorPane) {
            showViewNewStage("/com/tienda/login/Login.fxml", "Inicio de sesion");
        } else {
            // Manejo para otros tipos de contenedores o situaciones.
        }
    }

    @Override
    public void showRegisterUser() {
//        switchView("/com/tienda/User/UserRegister.fxml", "Registro de usuario");
        switchView("/com/tienda/login/UserRegister.fxml", "Registro de usuario");

    }

    @Override
    public void showMain() {
        showViewNewStage("/com/tienda/main/Sidebar.fxml", "Main");
//        showViewNewStage("/com/tienda/Main/MainView.fxml", "Main");
    }


    @Override
    public void showUserManagement() {
        switchView("/com/tienda/user/UserManagement.fxml", "Administracion de usuario");
    }

    @Override
    public void showMainView() {
        if (mainContainer instanceof AnchorPane) {
            switchView("/com/tienda/main/MainView.fxml", "Vista principal");
        }

//        switchViewWithCircularReveal("/com/tienda/main/MainView.fxml", "Vista principal");
    }


}
