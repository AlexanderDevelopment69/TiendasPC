package com.tienda.ControllerGUI.Components;

import com.jfoenix.controls.JFXHamburger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class Sidebar extends BorderPane {

//    @FXML
    private VBox sidebarVBox = new VBox();
//    @FXML
    private JFXHamburger hamburgerButton = new JFXHamburger();

    private boolean isExpanded = false;
    private double expandedWidth = 200;
    private double collapsedWidth = 55;




    private void initializeSidebar()  {
        //        // Configura el botón de hamburguesa para mostrar/ocultar el Sidebar
//        hamburgerButton.setOnAction(e -> {
//            if (!isExpanded) {
//                expandSidebar();
//            } else {
//                collapseSidebar();
//            }
//            isExpanded = !isExpanded;
//        });


//        // Configurar el botón de hamburguesa para mostrar/ocultar el Sidebar
//        HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburgerButton);
//        burgerTask.setRate(-1); // Configurar la velocidad para revertir la animación


        // Configurar el botón de hamburguesa para mostrar/ocultar el Sidebar
        hamburgerButton.setOnMouseClicked(e -> {
            if (!isExpanded) {
                expandSidebar();
//                burgerTask.setRate(burgerTask.getRate() * -1); // Invertir la animación
//                burgerTask.play();
                // Mover el JFXHamburger a la esquina superior derecha
                VBox.setMargin(hamburgerButton, new Insets(20, 0, 0, 150)); // Ajusta los márgenes según sea necesario
            } else {
                collapseSidebar();
//                burgerTask.setRate(burgerTask.getRate() * -1); // Invertir la animación
//                burgerTask.play();
                // Mover el JFXHamburger a la esquina superior izquierda
                VBox.setMargin(hamburgerButton, new Insets(20, 0, 0, 0)); // Ajusta los márgenes según sea necesario
            }
            /**
             * Cambia el estado de expansión. Si el estado actual es "expandido" (true), lo cambia a "contraído" (false).
             * Si el estado actual es "contraído" (false), lo cambia a "expandido" (true).
             */
            isExpanded = !isExpanded;
        });



    }


    /**
     * Expande suavemente el panel lateral (sidebar) con una animación suave.
     * La animación tiene una duración de 400 milisegundos (0.4 segundos).
     * Durante la animación, el ancho mínimo y máximo del panel lateral se ajusta para hacerlo más ancho,
     * creando así un efecto de expansión suave.
     */
    private void expandSidebar() {
        // Animación para expandir suavemente el sidebar con una duración de 400 ms

        // Crear una secuencia de animación utilizando la clase Timeline
        Timeline timeline = new Timeline(
                // Definir un fotograma clave (KeyFrame) con una duración de 400 milisegundos
                new KeyFrame(Duration.millis(400),
                        // Cambiar el ancho mínimo y máximo del panel lateral al valor de 'expandedWidth' (expansión completa)
                        new KeyValue(sidebarVBox.minWidthProperty(), expandedWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                )
        );
        // Iniciar la animación
        timeline.play();
    }

    /**
     * Contrae suavemente el panel lateral (sidebar) con una animación suave.
     * La animación tiene una duración de 400 milisegundos (0.4 segundos).
     * Durante la animación, el ancho mínimo del panel lateral se ajusta para hacerlo más estrecho,
     * creando así un efecto de contracción suave.
     */
    private void collapseSidebar() {
        // Animación para contraer suavemente el sidebar con una duración de 400 ms
        // Crear una secuencia de animación utilizando la clase Timeline
        Timeline timeline = new Timeline(
                // Definir un fotograma clave (KeyFrame) con una duración de 400 milisegundos
                new KeyFrame(Duration.millis(400),
                        // Cambiar el ancho mínimo del panel lateral al valor de 'collapsedWidth' (contracción completa)
                        new KeyValue(sidebarVBox.minWidthProperty(), collapsedWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                )
        );
        // Iniciar la animación
        timeline.play();
    }


    /*
La función expandSidebar() se encarga de animar la expansión del panel lateral (sidebar) en una interfaz de usuario JavaFX. Vamos a desglosar paso a paso lo que hace esta función:
1.	Creación de una Secuencia de Animación: La función comienza creando una instancia de la clase Timeline. Timeline es una clase en JavaFX que permite crear secuencias de animación.
2.	Definición de Fotogramas Clave (KeyFrames): Dentro de la instancia de Timeline, se define un fotograma clave (KeyFrame). Un KeyFrame es un instante de tiempo específico en una animación.
3.	Duración de la Animación: Se especifica la duración de este fotograma clave con Duration.millis(400). Esto significa que la animación completa tomará 400 milisegundos (0.4 segundos) para ejecutarse.
4.	Definición de Cambios (KeyValues): Dentro del fotograma clave, se define qué cambios deben ocurrir durante la animación. En este caso, se utilizan KeyValue para cambiar dos propiedades del panel lateral (sidebarVBox):
•	sidebarVBox.minWidthProperty(): Esto se refiere al ancho mínimo del panel lateral. Durante la animación, este valor cambiará gradualmente.
•	expandedWidth: expandedWidth es el valor al que se expandirá el ancho mínimo del panel lateral. En otras palabras, es el ancho al que se quiere que el panel lateral se expanda.
5.	Efecto de Interpolación (Interpolator): Se aplica una función de interpolación a la animación para que el cambio de tamaño ocurra suavemente. En este caso, se utiliza Interpolator.SPLINE(0.25, 0.1, 0.25, 1). Esta interpolación específica crea una aceleración y desaceleración suaves durante la animación, lo que resulta en un efecto de expansión suave.
6.	Iniciar la Animación: Finalmente, se llama al método play() en la instancia de Timeline. Esto inicia la animación.
En resumen, expandSidebar() crea una animación que cambia suavemente el ancho mínimo del panel lateral (sidebarVBox) desde su tamaño actual hasta un tamaño específico (expandedWidth) durante un período de 400 milisegundos. El resultado es un efecto de expansión suave del panel lateral en la interfaz de usuario. Esta función se utiliza generalmente en la implementación de paneles laterales desplegables o menús expandibles en aplicaciones JavaFX.
*/


    /*
    La función collapseSidebar() se encarga de animar la contracción del panel lateral (sidebar) en una interfaz de usuario JavaFX. A continuación, desglosaremos en detalle lo que hace esta función:
1.	Creación de una Secuencia de Animación: La función comienza creando una instancia de la clase Timeline. Timeline es una clase en JavaFX que permite crear secuencias de animación.
2.	Definición de Fotogramas Clave (KeyFrames): Dentro de la instancia de Timeline, se define un fotograma clave (KeyFrame). Un KeyFrame es un instante de tiempo específico en una animación.
3.	Duración de la Animación: Se especifica la duración de este fotograma clave con Duration.millis(400). Esto significa que la animación completa tomará 400 milisegundos (0.4 segundos) para ejecutarse.
4.	Definición de Cambios (KeyValues): Dentro del fotograma clave, se define qué cambios deben ocurrir durante la animación. En este caso, se utiliza KeyValue para cambiar una propiedad del panel lateral (sidebarVBox):
•	sidebarVBox.minWidthProperty(): Esto se refiere al ancho mínimo del panel lateral. Durante la animación, este valor cambiará gradualmente.
•	collapsedWidth: collapsedWidth es el valor al que se contraerá el ancho mínimo del panel lateral. Es el ancho al que se quiere que el panel lateral se contraiga.
5.	Efecto de Interpolación (Interpolator): Se aplica una función de interpolación a la animación para que el cambio de tamaño ocurra suavemente. En este caso, se utiliza Interpolator.SPLINE(0.25, 0.1, 0.25, 1). Esta interpolación específica crea una aceleración y desaceleración suaves durante la animación, lo que resulta en un efecto de contracción suave.
6.	Iniciar la Animación: Finalmente, se llama al método play() en la instancia de Timeline. Esto inicia la animación.
En resumen, collapseSidebar() crea una animación que cambia suavemente el ancho mínimo del panel lateral (sidebarVBox) desde su tamaño actual hasta un tamaño específico (collapsedWidth) durante un período de 400 milisegundos. El resultado es un efecto de contracción suave del panel lateral en la interfaz de usuario. Esta función se utiliza comúnmente en la implementación de paneles laterales desplegables o menús colapsables en aplicaciones JavaFX.


    */


}
