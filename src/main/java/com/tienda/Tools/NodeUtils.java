package com.tienda.Tools;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Clase utilitaria para maximizar y restaurar cualquier nodo en JavaFX.
 */
public class NodeUtils {

    // Variables para almacenar la posición y dimensiones originales del Stage
    private static double originalX, originalY, originalWidth, originalHeight;

    /**
     * Alterna entre maximizar y restaurar un nodo en su ventana asociada (Stage).
     *
     * @param node  El nodo que se maximizará o restaurará.
     */
    public static void toggleMaximize(Node node) {
        // Obtener el Stage al que pertenece el nodo
        Stage stage = (Stage) node.getScene().getWindow();

        // Verificar si el Stage está maximizado
        if (stage.isMaximized()) {
            // Si está maximizado, restaurar a su tamaño y posición originales
            restore(stage);
        } else {
            // Si no está maximizado, maximizar al tamaño de la pantalla
            maximize(node, stage);
        }
    }

    // Método privado para maximizar el Stage al tamaño de la pantalla
    private static void maximize(Node node, Stage stage) {
        // Almacenar la posición y dimensiones originales del Stage
        originalX = stage.getX();
        originalY = stage.getY();
        originalWidth = stage.getWidth();
        originalHeight = stage.getHeight();

        // Obtener información sobre la pantalla principal
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Configurar el Stage para ocupar toda la pantalla
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        // Establecer el Stage como maximizado
        stage.setMaximized(true);
    }

    // Método privado para restaurar el Stage a su tamaño y posición originales
    private static void restore(Stage stage) {
        // Desactivar la maximización
        stage.setMaximized(false);

        // Restaurar la posición y dimensiones originales
        stage.setX(originalX);
        stage.setY(originalY);
        stage.setWidth(originalWidth);
        stage.setHeight(originalHeight);
    }
}
