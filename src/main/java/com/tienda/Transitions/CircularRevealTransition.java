package com.tienda.Transitions;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.Interpolator;

/**
 * Esta clase representa una transición de revelación circular que puede utilizarse
 * para animar suavemente la aparición de contenido en una vista.
 */
public class CircularRevealTransition extends Group {
    private Circle revealClip;  // El círculo que se utilizará para revelar el contenido
    private Group contentGroup; // El grupo que contendrá el contenido a revelar


    /**
     * Constructor de la clase CircularRevealTransition.
     *
     * @param centerX La coordenada X del centro del círculo de revelación.
     * @param centerY La coordenada Y del centro del círculo de revelación.
     * @param radius  El radio inicial del círculo de revelación.
     */
    public CircularRevealTransition(double centerX, double centerY, double radius) {
        // Crear un nuevo círculo (revealClip) con las siguientes características:
        revealClip = new Circle(centerX, centerY, 0);

        // Establece el color de relleno del círculo (revealClip) en negro.
        revealClip.setFill(Color.BLACK);

        // Establecer la opacidad del círculo (revealClip) en cero (totalmente transparente).
        revealClip.setOpacity(0);


        // Crea un grupo para contener el contenido
        contentGroup = new Group();
        // Establece un clip en el contentGroup utilizando el círculo revealClip
        contentGroup.setClip(revealClip);


        /**
         * Crea un nuevo rectángulo que servirá como fondo de la transición de revelación circular.
         *
         * @param centerX La coordenada x del centro del círculo de revelación.
         * @param centerY La coordenada y del centro del círculo de revelación.
         * @param radius El radio del círculo de revelación.
         * @return El rectángulo de fondo transparente.
         */
        Rectangle background = new Rectangle(centerX - radius, centerY - radius, radius * 2, radius * 2);
        // Establecer el relleno del rectángulo como transparente.
        background.setFill(Color.web("#5771F7"));


        // Agrega el fondo y el grupo de contenido al Group principal
        getChildren().addAll(background, contentGroup);


        /**
         * Crea una nueva línea de tiempo para la animación de revelación circular.
         */
        Timeline timeline = new Timeline();


        // Define las propiedades de animación de radio y opacidad con interpolación personalizada
        KeyValue clipRadiusValue = new KeyValue(
                revealClip.radiusProperty(), // Propiedad que se animará: radio de revealClip
                radius, // Valor final del radio (tamaño máximo)
                Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
        );
        KeyValue clipOpacityValue = new KeyValue(
                revealClip.opacityProperty(), // Propiedad que se animará: opacidad de revealClip
                1, // Valor final de opacidad (completamente visible)
                Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
        );


        // Define el fotograma clave que utiliza los valores clave y la duración
        KeyFrame keyFrame = new KeyFrame(Duration.millis(900), clipRadiusValue, clipOpacityValue);

        // Agrega el fotograma clave a la línea de tiempo
        timeline.getKeyFrames().add(keyFrame);
        // Establece la cantidad de ciclos de la animación
        timeline.setCycleCount(1);

        // Inicia la animación
        timeline.play();
    }

    /**
     * Establece el contenido que se revelará durante la transición.
     *
     * @param content El nodo de contenido que se mostrará durante la transición.
     */
    public void setContent(Node content) {
        // Agrega un nodo (content) al grupo contentGroup
        contentGroup.getChildren().add(content);
    }




      /*
        El código Interpolator.SPLINE(0.25, 0.1, 0.25, 1) se utiliza para definir una interpolación personalizada en una animación. Esta interpolación personalizada determina cómo cambian los valores a lo largo del tiempo durante la animación. Los números proporcionados (0.25, 0.1, 0.25, 1) son los puntos de control que ajustan el comportamiento de la interpolación.
        En este caso, los puntos de control (0.25, 0.1, 0.25, 1) representan una interpolación cúbica de Bezier. Cada número tiene un significado específico:

        El primer número (0.25) controla la posición del punto de control inicial en el eje x.
        El segundo número (0.1) controla la posición del punto de control inicial en el eje y.
        El tercer número (0.25) controla la posición del punto de control final en el eje x.
        El cuarto número (1) controla la posición del punto de control final en el eje y.
        Estos valores ajustan la velocidad y la aceleración de la animación en diferentes momentos. En este caso, la interpolación personalizada se ha configurado de manera que la animación sea más lenta al principio y más rápida hacia el final, lo que puede dar lugar a una transición suave y natural.
        * */


}
