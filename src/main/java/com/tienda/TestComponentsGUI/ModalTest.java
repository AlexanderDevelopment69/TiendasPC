package com.tienda.TestComponentsGUI;

import com.tienda.ControllerGUI.Components.ModalDialog;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ModalTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        ModalDialog modal = new ModalDialog();
//        modal.setIconImage(new Image("Images/iconCheck.png"));
//        modal.setTitle("Registrado correctamente.");
//        modal.setMessage("Registrado correctamente, ahora \n" +
//                "puedes ingresar al sistema.");
//        modal.setConfirmButtonText("Confirmar");
//        modal.setExitButtonText("Cancelar");
//
//        // Configurar acciones para los botones
//        modal.setExitButtonAction(event -> {
//            // Acción cuando se hace clic en el botón de salida
//            System.out.println("Clic en el botón de salida");
//            // Aquí puedes agregar el código que desees ejecutar al hacer clic en el botón de salida
//        });
//
//        modal.setConfirmButtonAction(event -> {
//            // Acción cuando se hace clic en el botón de confirmación
//            System.out.println("Clic en el botón de confirmación");
//            // Aquí puedes agregar el código que desees ejecutar al hacer clic en el botón de confirmación
//        });


        //Crear una instancia del modal
        ModalDialog modalDialog = new ModalDialog();
        // Configurar el modal mediante un solo método
        modalDialog.configureModal(
                new Image("Images/iconCheck.png"),
                "Registrado correctamente.",
                "Registrado correctamente, ahora \npuedes ingresar al sistema.",
                "Confirmar",
                "Cancelar",
                event -> {
                    // Lógica cuando se hace clic en el botón "Confirmar"
                    System.out.println("Se hizo clic en Confirmar");
//                    modalDialog.close(); // Cierra el modal
                },
                event -> {
                    // Lógica cuando se hace clic en el botón "Cancelar"
                    System.out.println("Se hizo clic en Cancelar");
//                    modalDialog.close(); // Cierra el modal
                }
        );


        Scene scene = new Scene(modalDialog, 400, 350);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
