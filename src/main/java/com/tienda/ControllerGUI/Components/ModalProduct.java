package com.tienda.ControllerGUI.Components;

import com.tienda.Configs.HibernateUtil;
import com.tienda.Dao.ProductCategoryDAO;
import com.tienda.Dao.ProductCategoryDAOHibernate;
import com.tienda.Dao.SupplierDAO;
import com.tienda.Dao.SupplierDAOHibernate;
import com.tienda.Tools.MFXTextFieldValidator;
import com.tienda.Tools.TextFieldValidator;
import com.tienda.dto.ProductCategoryDTO;
import com.tienda.dto.SupplierDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.stream.EventFilter;
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
    private MFXLegacyComboBox<String> cbProduct;

    @FXML
    private MFXLegacyComboBox<String> cbSupplier;

    @FXML
    private MFXButton confirmButton;

    @FXML
    private ImageView productImage;

    @FXML
    private Label labelTitleImagen;
    @FXML
    private Label labelDescriptionModal;

    @FXML
    private Label labelTitleModal;

    @FXML
    private MFXTextField txtBrand;

    @FXML
    private TextArea txtDescription;

    @FXML
    private MFXTextField txtPath;

    @FXML
    private MFXTextField txtPrice;

    @FXML
    private MFXTextField txtProduct;

    @FXML
    private MFXButton uploadButton;

    private String imagePATH;

    @FXML
    private Label labelValidatePrice;


    ProductCategoryDAO productCategoryDAO;
    SupplierDAO supplierDAO;
    List<ProductCategoryDTO> categoryList;
    List<SupplierDTO> supplierList;


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
                cbProduct.getItems().add(productCategoryDTO.getCategoryName());
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
        // Obtener el nombre seleccionado en el ComboBox
        String selectedCategoryName = cbProduct.getValue();

        // Llamar al método getAllCategories() para obtener la lista de categorías
//        List<ProductCategoryDTO> categoryList = productCategoryDAO.getAllCategories();

        // Buscar el ID correspondiente al nombre seleccionado en la lista de categorías
        for (ProductCategoryDTO productCategoryDTO : categoryList) {
            // Comparar el nombre seleccionado con el nombre de la categoría actual
            if (selectedCategoryName.equals(productCategoryDTO.getCategoryName())) {
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
                txtPath.setText(nombreImagen);

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

    public byte[] getImageToByteArray() {
        return imageToByteArray(imagePATH);
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
            txtPath.setText(nombreImagen);

            // Mostrar la imagen en el ImageView
            productImage.setImage(image); // Establecer la imagen en el ImageView


            return selectedFile.getAbsolutePath(); // Devuelve la ruta de la imagen seleccionada
        } else {
            System.out.println("No se seleccionó ninguna imagen.");
            return null; // Devuelve null si no se selecciona ninguna imagen
        }
    }

    /**
     * Convierte una imagen en un arreglo de bytes a partir de su ruta de archivo.
     *
     * @param imagePath La ruta de archivo de la imagen a convertir en bytes.
     * @return Un arreglo de bytes que representa la imagen o null si ocurre un error.
     */
    public static byte[] imageToByteArray(String imagePath) {
        // Comprobar si la ruta de la imagen es válida
        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Ruta de imagen no válida.");
            return null;
        }

        try {
            // Abrir el archivo de imagen
            File file = new File(imagePath); // Crear un objeto File con la ruta proporcionada
            FileInputStream fis = new FileInputStream(file); // Crear un flujo de entrada para leer el archivo
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); // Crear un flujo de salida para almacenar los bytes

            byte[] buffer = new byte[1024]; // Crear un búfer de bytes para la lectura
            int bytesRead;

            // Leer y escribir en un arreglo de bytes
            while ((bytesRead = fis.read(buffer)) != -1) { // Leer bytes del archivo hasta el final
                bos.write(buffer, 0, bytesRead); // Escribir los bytes en el flujo de salida
            }

            fis.close(); // Cerrar el flujo de entrada
            bos.close(); // Cerrar el flujo de salida

            return bos.toByteArray(); // Devuelve el arreglo de bytes que representa la imagen
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza de la excepción en caso de error
            System.err.println("Error al cargar la imagen: " + e.getMessage()); // Imprimir un mensaje de error
            return null; // Manejo de errores, devuelve null en caso de error
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


    public String getTxtBrand() {
        return txtBrand.getText();
    }

    public String getTxtDescription() {
        return txtDescription.getText();
    }

    public String getTxtPrice() {
        return txtPrice.getText();
    }

    public String getTxtProduct() {
        return txtProduct.getText();
    }

    public void setTxtBrand(String brand) {
        txtBrand.setText(brand);
    }

    public void setTxtDescription(String description) {
        txtDescription.setText(description);
    }

    public void setTxtPrice(String price) {
        txtPrice.setText(price);
    }

    public void setTxtProduct(String productName) {
        txtProduct.setText(productName);
    }

    public void setValueCbProductCategory(String value) {
        cbProduct.setValue(value);
    }

    public void setValueCbSupplier(String value) {
        cbSupplier.setValue(value);
    }

    /**
     * Valida si un ComboBox tiene un valor seleccionado.
     * @return true si el ComboBox tiene un valor seleccionado; false si está vacío o ningún valor está seleccionado.
     */
    public boolean validateComboBox() {
        // Verificar si el ComboBox tiene un valor seleccionado o está vacío
        return cbProduct.getValue() != null || cbSupplier.getValue()!=null ;
    }

    public void setConfirmButtonText(String text) {
        confirmButton.setText(text);
    }


    public void configureModal(String titleModal,String description,String nameButton, EventHandler<ActionEvent> confirmButtonAction) {

        labelTitleModal.setText(titleModal);
        labelDescriptionModal.setText(description);
        setConfirmButtonText(nameButton);
        setConfirmButtonAction(confirmButtonAction);
        txtProduct.setEditable(false);
        txtBrand.setEditable(false);
        txtDescription.setEditable(false);
        txtPrice.setEditable(false);

        anchorPane.getChildren().remove(cancelButton);
        anchorPane.getChildren().remove(uploadButton);
        anchorPane.getChildren().remove(labelTitleImagen);
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


        TextFieldValidator validator = new TextFieldValidator(txtPrice, labelValidatePrice, TextFieldValidator.ValidationCriteria.PRICE);


    }
}
