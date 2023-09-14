package com.tienda.Tools;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper {
    public static double xOffset = 0;
    public static double yOffset = 0;

    private static double minWidth = 950;
    private static double minHeight = 620;
    private static boolean isResizing = false;

    public static void setResizeHandlers(Stage stage, Node root) {
        Scene scene = stage.getScene();
        if (scene == null) {
            throw new IllegalStateException("Scene must be set on the Stage before setting resize handlers.");
        }

        // Evento de movimiento del ratón para cambiar el cursor cuando está sobre una esquina
        scene.setOnMouseMoved(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = root.getBoundsInLocal().getWidth();
            double height = root.getBoundsInLocal().getHeight();

            if (x <= 10 && y <= 10) {
                isResizing = true;
                scene.setCursor(Cursor.NW_RESIZE);
            } else if (x >= width - 10 && y <= 10) {
                isResizing = true;
                scene.setCursor(Cursor.NE_RESIZE);
            } else if (x <= 10 && y >= height - 10) {
                isResizing = true;
                scene.setCursor(Cursor.SW_RESIZE);
            } else if (x >= width - 10 && y >= height - 10) {
                isResizing = true;
                scene.setCursor(Cursor.SE_RESIZE);
            } else {
                isResizing = false;
                scene.setCursor(Cursor.DEFAULT);
            }
        });

        // Evento de clic del ratón para determinar si se está redimensionando
        scene.setOnMouseDragged(event -> {
            if (isResizing) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                double width = stage.getWidth();
                double height = stage.getHeight();

                if (scene.getCursor() == Cursor.NW_RESIZE) {
                    double newWidth = width + (xOffset - x);
                    double newHeight = height + (yOffset - y);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setX(x);
                        stage.setY(y);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (scene.getCursor() == Cursor.NE_RESIZE) {
                    double newWidth = width + (x - width);
                    double newHeight = height + (yOffset - y);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setY(y);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (scene.getCursor() == Cursor.SW_RESIZE) {
                    double newWidth = width + (xOffset - x);
                    double newHeight = height + (y - height);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setX(x);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (scene.getCursor() == Cursor.SE_RESIZE) {
                    double newWidth = width + (x - width);
                    double newHeight = height + (y - height);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                }
            }
        });

        // Evento de liberación del ratón para finalizar el redimensionamiento
        scene.setOnMouseReleased(event -> {
            isResizing = false;
            scene.setCursor(Cursor.DEFAULT);
        });
    }

    public static void setMoveHandlers(Stage stage) {
        Scene scene = stage.getScene();
        if (scene == null) {
            throw new IllegalStateException("Scene must be set on the Stage before setting move handlers.");
        }

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!isResizing) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }
}
