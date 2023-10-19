package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.ProductCategoryDAO;
import com.tienda.DaoImpl.ProductCategoryDAOHibernate;
import com.tienda.Dao.SupplierDAO;
import com.tienda.DaoImpl.SupplierDAOHibernate;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.dto.ProductCategoryDTO;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModalProduct extends StackPane implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MFXButton cancelButton;

    @FXML
    public MFXLegacyComboBox<String> cbProductCategory;

    @FXML
    public MFXLegacyComboBox<String> cbSupplier;

    @FXML
    private MFXButton confirmButton;

    @FXML
    public ImageView productImage;

    @FXML
    private Label labelTitleImagen;
    @FXML
    private Label labelDescriptionModal;

    @FXML
    private Label labelTitleModal;

    @FXML
    public MFXTextField textBrand;

    @FXML
    public TextArea textDescription;

    @FXML
    public MFXTextField textPath;

    @FXML
    public MFXTextField textPrice;

    @FXML
    public MFXTextField textProductName;

    @FXML
    public MFXButton uploadButton;

    public String imagePATH;

    @FXML
    private Label labelValidatePrice;


    ProductCategoryDAO productCategoryDAO;
    SupplierDAO supplierDAO;
    List<ProductCategoryDTO> categoryList;
    List<SupplierDTO> supplierList;

    TextFieldValidator priceValidator;

    public ModalProduct() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tienda/components/ModalProduct.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public void setExitButtonAction(EventHandler<ActionEvent> action) {
        cancelButton.setOnAction(action);
    }

    public void setConfirmButtonAction(EventHandler<ActionEvent> action) {
        confirmButton.setOnAction(action);
    }

    public void setUploadButton(EventHandler<ActionEvent> action) {
        uploadButton.setOnAction(action);
    }

    public void addElementsProductCategoryToCombobox() {
        // Llamar al método gettAllCategories() y obtener la lista de categorias
        categoryList = productCategoryDAO.getAllCategories();
        // Verificar si la lista categoria  no es nula.
        if (categoryList != null) {
            // Iterar a través de la lista  e imprimir cada categoria .
            for (ProductCategoryDTO productCategoryDTO : categoryList) {
                cbProductCategory.getItems().add(productCategoryDTO.getCategoryName());
            }
        }
    }

    public void addElementsSuppliersToCombobox() {
        // Llamar al método gettgetAllSuppliers() y obtener la lista de proveedores
        supplierList = supplierDAO.getAllSuppliers();
        // Verificar si la lista categoria  no es nula.
        if (supplierList != null) {
            // Iterar a través de la lista  e imprimir cada proveedor .
            for (SupplierDTO supplierDTO : supplierList) {
                cbSupplier.getItems().add(supplierDTO.getSupplierName());
            }
        }

    }


    /**
     * Método para obtener el ID seleccionado del ComboBox.
     *
     * @return El ID (como un valor Long) correspondiente al nombre seleccionado en el ComboBox.
     * Si no se encuentra un ID correspondiente, se devuelve null.
     */
    public Long getValueSelectedProductCategory() {
        if (cbProductCategory.getSelectionModel().getSelectedItem()==null){
            return null;
        }


        // Obtener el nombre seleccionado en el ComboBox
        String selectedProductCategoryName = cbProductCategory.getValue();

        // Llamar al método getAllCategories() para obtener la lista de categorías
//        List<ProductCategoryDTO> categoryList = productCategoryDAO.getAllCategories();

        // Buscar el ID correspondiente al nombre seleccionado en la lista de categorías
        for (ProductCategoryDTO productCategoryDTO : categoryList) {
            // Comparar el nombre seleccionado con el nombre de la categoría actual
            if (selectedProductCategoryName.equals(productCategoryDTO.getCategoryName())) {
                // Si se encuentra una coincidencia, devolver el ID de la categoría
                return productCategoryDTO.getCategoryId();
            }
        }

        // Devolver null si no se encuentra un ID correspondiente al nombre seleccionado
        return null;
    }


    /**
     * Método para obtener el ID seleccionado del ComboBox.
     *
     * @return El ID (como un valor Long) correspondiente al nombre seleccionado en el ComboBox.
     * Si no se encuentra un ID correspondiente, se devuelve null.
     */
    public Long getValueSelectedSupplier() {
        if (cbSupplier.getSelectionModel().getSelectedItem()==null){
            return null;
        }

        // Obtener el nombre seleccionado en el ComboBox
        String selectedSupplierName = cbSupplier.getValue();

//        // Llamar al método getAllCategories() para obtener la lista de categorías
//        List<SupplierDTO> supplierList = supplierDAO.getAllSuppliers();

        // Buscar el ID correspondiente al nombre seleccionado en la lista de categorías
        for (SupplierDTO supplierDTO : supplierList) {
            // Comparar el nombre seleccionado con el nombre de la categoría actual
            if (selectedSupplierName.equals(supplierDTO.getSupplierName())) {
                // Si se encuentra una coincidencia, devolver el ID de la categoría
                return supplierDTO.getSupplierId();
            }
        }

        // Devolver null si no se encuentra un ID correspondiente al nombre seleccionado
        return null;
    }


    /**
     * Abre un cuadro de diálogo para seleccionar una imagen y la muestra en un ImageView.
     * Si ocurre un error al cargar la imagen, se maneja y se imprime un mensaje de error.
     */
    public void uploadImage() {
        // Configurar el cuadro de diálogo para seleccionar archivos de imagen
        FileChooser fileChooser = new FileChooser(); // Crear un nuevo FileChooser
        fileChooser.setTitle("Seleccionar una imagen"); // Establecer el título del cuadro de diálogo
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de imagen (*.png, *.jpg)", "*.png", "*.jpg"); // Establecer un filtro para tipos de archivo
        fileChooser.getExtensionFilters().add(extFilter); // Agregar el filtro al cuadro de diálogo

        // Mostrar el cuadro de diálogo de selección de archivo y obtener la imagen seleccionada
        File file = fileChooser.showOpenDialog(null); // Mostrar el cuadro de diálogo

        // Si se selecciona un archivo
        if (file != null) {
            try {
                // Cargar la imagen seleccionada
                Image image = new Image(file.toURI().toString()); // Crear un objeto Image a partir del archivo seleccionado

                // Obtener el nombre del archivo
                String nombreImagen = file.getName();

                // Mostrar el nombre del archivo en un Label
                textPath.setText(nombreImagen);

                // Verificar si hay un error al cargar la imagen
                if (image.isError()) { // Comprobar si la carga de la imagen generó un error
                    System.err.println("Error al cargar la imagen."); // Imprimir un mensaje de error
                    return; // Salir del método
                }

                // Mostrar la imagen en el ImageView
                productImage.setImage(image); // Establecer la imagen en el ImageView
            } catch (Exception e) {
                e.printStackTrace(); // Imprimir la traza de la excepción en caso de error
                System.err.println("Error al cargar la imagen: " + e.getMessage()); // Imprimir un mensaje de error con detalles
            }
        }
    }


    public void getPathImage() {
        imagePATH = findImagePath();
    }


    /**
     * Abre un cuadro de diálogo de selección de archivo para permitir al usuario
     * seleccionar una imagen y devuelve la ruta de archivo de la imagen seleccionada.
     *
     * @return La ruta de archivo de la imagen seleccionada o null si no se selecciona ninguna imagen.
     */
    public String findImagePath() {
        // Configurar el selector de archivos para imágenes
        FileChooser fileChooser = new FileChooser(); // Crear un objeto FileChooser
        fileChooser.setTitle("Seleccionar una imagen"); // Establecer título del cuadro de diálogo
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de imagen (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif"); // Filtro de extensión de archivos
        fileChooser.getExtensionFilters().add(extFilter); // Agregar el filtro al cuadro de diálogo

        // Mostrar el cuadro de diálogo de selección de archivo y obtener la imagen seleccionada
        File selectedFile = fileChooser.showOpenDialog(new Stage()); // Mostrar el cuadro de diálogo en una nueva ventana

        if (selectedFile != null) {
            // Cargar la imagen seleccionada
            Image image = new Image(selectedFile.toURI().toString()); // Crear un objeto Image a partir del archivo seleccionado

            // Obtener el nombre del archivo
            String nombreImagen = selectedFile.getName();

            // Mostrar el nombre del archivo en un Label
            textPath.setText(nombreImagen);

            // Mostrar la imagen en el ImageView
            productImage.setImage(image); // Establecer la imagen en el ImageView


            return selectedFile.getAbsolutePath(); // Devuelve la ruta de la imagen seleccionada
        } else {
            System.out.println("No se seleccionó ninguna imagen.");
            return null; // Devuelve null si no se selecciona ninguna imagen
        }
    }

    private static final float COMPRESSION_QUALITY = 0.2f; // Ajusta la calidad de compresión (0.0f - 1.0f)

    public byte[] imageToByteArray() {
        try {
            // Verificar si la ruta de la imagen es válida
            if (imagePATH == null || imagePATH .isEmpty()) {
                System.err.println("Ruta de imagen no válida.");
                return null;
            }

            File file = new File(imagePATH);
            BufferedImage bufferedImage = ImageIO.read(file);

            // Crear un flujo de salida de bytes para almacenar la imagen comprimida en formato JPEG
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                // Obtener un escritor de imágenes JPEG
                ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();

                // Configurar parámetros de compresión
                ImageWriteParam imageWriteParam = new JPEGImageWriteParam(null);
                imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                imageWriteParam.setCompressionQuality(COMPRESSION_QUALITY);

                // Obtener un flujo de salida de imágenes
                ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
                imageWriter.setOutput(imageOutputStream);

                // Escribir la imagen comprimida en el flujo de salida de bytes
                imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

                // Cerrar los flujos
                imageOutputStream.close();
                imageWriter.dispose();

                return byteArrayOutputStream.toByteArray(); // Devuelve el arreglo de bytes que representa la imagen comprimida
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al comprimir la imagen.");
            return null;
        }
    }





    /**
     * Convierte una imagen en un arreglo de bytes en formato JPEG con calidad ajustable.
     *
     * @param image La imagen de JavaFX que se desea convertir.
     * @return Un arreglo de bytes que representa la imagen comprimida en formato JPEG o null si ocurre un error.
     */
    public  byte[] imageToByteArray(Image image) {
        try {
            if (image == null) {
                System.err.println("La imagen es nula.");
                return null;
            }

            // Convierte la imagen de JavaFX en una BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Convierte a espacio de color RGB
            BufferedImage rgbImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = rgbImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, null);
            g.dispose();

            // Crear un flujo de salida de bytes para almacenar la imagen comprimida en formato JPEG
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                // Obtener un escritor de imágenes JPEG
                ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();

                // Configurar parámetros de compresión JPEG
                ImageWriteParam imageWriteParam = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
                imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                imageWriteParam.setCompressionQuality(COMPRESSION_QUALITY);

                // Establecer el flujo de salida del escritor de imágenes
                imageWriter.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));

                // Escribir la imagen comprimida en el flujo de salida de bytes
                imageWriter.write(null, new javax.imageio.IIOImage(rgbImage, null, null), imageWriteParam);

                // Liberar recursos del escritor de imágenes
                imageWriter.dispose();

                // Devuelve el arreglo de bytes que representa la imagen comprimida
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al comprimir la imagen.");
            return null;
        }
    }




    // Método para mostrar un arreglo de bytes en un ImageView
    public void showImageInImageView(byte[] imagenComoBytes) {
        if (imagenComoBytes != null) {
            // Crear una imagen a partir del arreglo de bytes
            Image image = new Image(new ByteArrayInputStream(imagenComoBytes));

            // Establecer la imagen en el ImageView
            productImage.setImage(image);
        } else {
            System.err.println("No se pudo cargar la imagen.");
        }
    }






    /**
     * Valida si un ComboBox tiene un valor seleccionado.
     * @return true si el ComboBox tiene un valor seleccionado; false si está vacío o ningún valor está seleccionado.
     */
    public boolean validateComboBox() {
        // Verificar si el ComboBox tiene un valor seleccionado o está vacío
        return cbProductCategory.getValue() != null || cbSupplier.getValue()!=null ;
    }


    public boolean validateTextFieldsProduct(List<String> errorMessages) {
        boolean validationSuccessful = true;
        String productName = textProductName.getText().trim();
        String brand = textBrand.getText().trim();
        String description = textDescription.getText().trim();
        String price = textPrice.getText().trim();
        Long productCategoryId = getValueSelectedProductCategory();
        Long supplierId = getValueSelectedSupplier();


        if (productName.isEmpty() || brand.isEmpty() ||  description.isEmpty() ||  price.isEmpty() ||  productCategoryId == null ||  supplierId == null){
            errorMessages.add("Completa todos los campos del producto, la imagen es opcional.\n");
            validationSuccessful = false;
        }

        if (validationSuccessful) {

            if (!price.isEmpty() && !priceValidator.validatePrice(price)) {
                errorMessages.add("El precio no cumple con los requisitos.\n");
                validationSuccessful = false;
            }


        }

        return validationSuccessful;
    }


    public void setConfirmButtonText(String text) {
        confirmButton.setText(text);
    }


    public void configureModal(String titleModal,String description,String nameButton, EventHandler<ActionEvent> confirmButtonAction) {

        labelTitleModal.setText(titleModal);
        labelDescriptionModal.setText(description);
        setConfirmButtonText(nameButton);
        setConfirmButtonAction(confirmButtonAction);
        textProductName.setEditable(false);
        textBrand.setEditable(false);
        textDescription.setEditable(false);
        textPrice.setEditable(false);

        anchorPane.getChildren().remove(cancelButton);
        anchorPane.getChildren().remove(uploadButton);
        anchorPane.getChildren().remove(labelTitleImagen);
        anchorPane.getChildren().remove(textPath);
    }

    public void configureModal(String titleModal,String description,String nameButton, EventHandler<ActionEvent> confirmButtonAction, EventHandler<ActionEvent> exitButtonAction, EventHandler<ActionEvent> uploadButton) {

        labelTitleModal.setText(titleModal);
        labelDescriptionModal.setText(description);
        setConfirmButtonText(nameButton);
        setConfirmButtonAction(confirmButtonAction);
        setExitButtonAction(exitButtonAction);
        setUploadButton(uploadButton);

    }




    private StackPane backdrop; // Nuevo componente para el fondo oscuro
    private boolean isClosed = false; // Variable para rastrear si el modal ya está cerrado

    public void showModal(StackPane parentStackPane) {
        // Añade el fondo oscuro al StackPane
        backdrop = createBackdrop(parentStackPane);
        parentStackPane.getChildren().add(backdrop);
        // Agrega un evento para cerrar el modal haciendo clic fuera de él
        backdrop.setOnMouseClicked(event -> close());


        // Calcula la posición para que el modal aparezca en el centro del StackPane
        double centerX = parentStackPane.getWidth() / 2;
        double centerY = parentStackPane.getHeight() / 2;

        // Establece la posición inicial del modal
        setLayoutX(centerX - (getPrefWidth() / 2));
        setLayoutY(centerY - (getPrefHeight() / 2));


        // Añade el modal al StackPane
        parentStackPane.getChildren().add(this);

        // Aplica la animación de zoom y opacidad con interpoladores personalizados
        setScaleX(0.7);
        setScaleY(0.7);
        setOpacity(0);

        // Interpolador personalizado para la escala
        Interpolator scaleInterpolator = Interpolator.SPLINE(0.4, 0.1, 0.2, 1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(scaleInterpolator);

        // Interpolador personalizado para la opacidad
        Interpolator opacityInterpolator = Interpolator.SPLINE(0.1, 0.1, 0.25, 1);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(1);
        fadeTransition.setInterpolator(opacityInterpolator);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();


    }

    private StackPane createBackdrop(StackPane parentStackPane) {
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Color de fondo oscuro
        backdrop.setPrefSize(parentStackPane.getWidth(), parentStackPane.getHeight());
        return backdrop;
    }

    public void close() {
        if (isClosed) {
            return; // Evitar cierre múltiple
        }

        isClosed = true; // Establecer la bandera de cierre

        // Aplica la animación inversa al cerrar el modal
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this);
        scaleTransition.setToX(0.1);
        scaleTransition.setToY(0.1);
        scaleTransition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), this);
        fadeTransition.setToValue(0);
        fadeTransition.setInterpolator(Interpolator.SPLINE(0.1, 0.1, 0.25, 1));

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> {
            StackPane parentStackPane = (StackPane) getParent();
            parentStackPane.getChildren().remove(backdrop);
            parentStackPane.getChildren().remove(this); // Remueve el modal
        });
        parallelTransition.play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productCategoryDAO = new ProductCategoryDAOHibernate(HibernateUtil.getSessionFactory());
        supplierDAO = new SupplierDAOHibernate(HibernateUtil.getSessionFactory());

        addElementsProductCategoryToCombobox();
        addElementsSuppliersToCombobox();

        priceValidator = new TextFieldValidator(textPrice, labelValidatePrice, TextFieldValidator.ValidationCriteria.PRICE);
    }
}
