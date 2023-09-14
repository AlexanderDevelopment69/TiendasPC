package com.tienda.Tools;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

public class ResponsiveLayoutHelper {

    public static void makeResponsive(Scene scene, Region... regions) {
        double totalWidth = scene.getWidth();
        double totalHeight = scene.getHeight();

        for (Region region : regions) {
            // Calcula el ancho y alto relativos para cada región
            double widthRatio = region.getPrefWidth() / totalWidth;
            double heightRatio = region.getPrefHeight() / totalHeight;

            // Vincula el ancho y alto de la región a los cambios de tamaño de la escena
            region.prefWidthProperty().bind(scene.widthProperty().multiply(widthRatio));
            region.prefHeightProperty().bind(scene.heightProperty().multiply(heightRatio));
        }
    }
}
