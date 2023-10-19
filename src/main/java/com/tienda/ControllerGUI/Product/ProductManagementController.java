package com.tienda.ControllerGUI.Product;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.ControllerGUI.Components.ModalProduct;
import com.tienda.ControllerGUI.Components.ModalProductCategory;
import com.tienda.Dao.*;
import com.tienda.DaoImpl.ProductCategoryDAOHibernate;
import com.tienda.DaoImpl.ProductDAOHibernate;
import com.tienda.Utils.ProductDataLoadingUtil;
import com.tienda.Utils.ProductUtil;
import com.tienda.dto.ProductCategoryDTO;
import com.tienda.dto.ProductDTO;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public class ProductManagementController implements Initializable {

    @FXML
    private AnchorPane centerAnchorPane;
    @FXML
    private TableColumn<ProductDTO, Void> colActions;

    @FXML
    private TableColumn<ProductDTO, String> colBrand;

    @FXML
    private TableColumn<ProductDTO, String> colCodigo;

    @FXML
    private TableColumn<ProductDTO, String> colStock;

    @FXML
    private TableColumn<ProductDTO, String> colDescription;

    @FXML
    private TableColumn<ProductDTO, String> colPrice;

    @FXML
    private TableColumn<ProductDTO, String> colProductName;

    @FXML
    private TableColumn<ProductDTO, String> colProductCategory;

    @FXML
    private TableColumn<ProductDTO, String> colSupplier;

    @FXML
    private MFXLegacyTableView<ProductDTO> productTable;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField textSearch;

    ProductDAO productDAO;
    ProductCategoryDAO productCategoryDAO;

    ProductDataLoadingUtil productDataLoadingUtil;

   ProductUtil productUtil;

    @FXML
    void handleAddProduct(ActionEvent event) {
        //Crear una instancia del modal
        ModalProduct modalProduct = new ModalProduct();


        // Configurar el modal mediante un solo método
        modalProduct.configureModal("Nuevo producto",
                "Completa todos los campos para registrar un nuevo producto.",
                "Registrar",
                ev1 -> {
                    String productName = modalProduct.textProductName.getText().trim();
                    String brand = modalProduct.textBrand.getText().trim();
                    String description = modalProduct.textDescription.getText().trim();
                    String price = modalProduct.textPrice.getText().trim();
                    byte[] image = modalProduct.imageToByteArray();
                    Long productCategoryId = modalProduct.getValueSelectedProductCategory();
                    Long supplierId = modalProduct.getValueSelectedSupplier();

                    List<String> errorMessages = new ArrayList<>();
                    boolean validationSuccessful = modalProduct.validateTextFieldsProduct(errorMessages);

                    if (validationSuccessful) {
                        ProductDTO newProductDTO = new ProductDTO();
                        newProductDTO.setProductName(productName);
                        newProductDTO.setProductBrand(brand);
                        newProductDTO.setProductDescription(description);
                        newProductDTO.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(price)));
                        newProductDTO.setAvailableStock(0);
                        newProductDTO.setProductCategoryId(productCategoryId);
                        newProductDTO.setSupplierId(supplierId);
                        newProductDTO.setProductImage(image);
                        productDAO.saveProduct(newProductDTO);
                        // Mostrar los datos en al tabla
                        productDataLoadingUtil.loadProductTableData(productTable);

                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(
                                new Image("Images/iconCheck.png"),
                                "Registrado correctamente.",
                                "Producto registrado correctamente.",
                                "OK",
                                e -> {
                                    // Lógica cuando se hace clic en el botón "Confirmar"
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();
                                }
                        );

                        modalDialog.showModal(root);

                    } else {
                        // Mostrar mensajes de error si existen problemas de validación
                        StringBuilder errorMessageBuilder = new StringBuilder();
                        for (String errorMessage : errorMessages) {
                            System.out.println(errorMessage);
                            errorMessageBuilder.append("- ").append(errorMessage).append("\n");
                        }

                        String errorMessageText = errorMessageBuilder.toString();

                        ModalDialog modalDialog = new ModalDialog();
                        modalDialog.configureModal(
                                new Image("Images/alert.png"),
                                "Registro incorrecto.",
                                "No se pudo registrar el producto correctamente: \n" + "\n" + errorMessageText,
                                "OK",
                                ev -> {
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();
                                }
                        );

                        modalDialog.showModal(root);

                    }


                },
                ev2 -> {
                    modalProduct.close(); // Cierra el modal
                },
                ev3 -> {
                    //Obtiene la ruta de la imagen
                    modalProduct.getPathImage();

                }

        );

        modalProduct.showModal(root);


    }


    @FXML
    void handleAddProductCategory(ActionEvent event) {
        //Crear una instancia del modal
        ModalProductCategory modalProductCategory = new ModalProductCategory();

        // Configurar el modal mediante un solo método
        modalProductCategory.configureModal(
                ev -> {
                    ProductCategoryDTO newProductCategoryDTO = new ProductCategoryDTO();


                    if (!modalProductCategory.getTxtCategoryName().isEmpty()) {

                        newProductCategoryDTO.setCategoryName(modalProductCategory.getTxtCategoryName());
                        productCategoryDAO.saveCategory(newProductCategoryDTO);

                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(
                                new Image("Images/iconCheck.png"),
                                "Registrado correctamente.",
                                "Categoria de producto registrado correctamente.",
                                "OK",
                                e -> {
                                    // Lógica cuando se hace clic en el botón "Confirmar"
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();
                                }
                        );

                        modalDialog.showModal(root);

                    } else {


                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(
                                new Image("Images/alert.png"),
                                "Registro incorrecto.",
                                "No se pudo registrar correctamente la categoria, completa el nombre de producto.",
                                "Volver atras",
                                e -> {
                                    // Lógica cuando se hace clic en el botón "Confirmar"
                                    System.out.println("Se hizo clic en Confirmar");
                                    modalDialog.close();

                                }

                        );

                        modalDialog.showModal(root);


                    }

                },
                ev -> {
                    modalProductCategory.close(); // Cierra el modal
                }


        );

        modalProductCategory.showModal(root);
    }

    @FXML
    void handleLoadDate() {
        productTable.getItems().clear();
        // Carga los datos en la TableView al inicializar la ventana
        productDataLoadingUtil.loadProductTableData(productTable);
    }


    private void configureTable() {
        // Agrega un EventHandler para deseleccionar la fila cuando el cursor esté fuera de la tabla
        productTable.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Deseleccionar la fila activa
                productTable.getSelectionModel().clearSelection();
            }
        });

        // Configura las celdas de la tablaalex
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("availableStock"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("productCategoryName"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        // Configura la columna de acciones
        colActions.setCellFactory(param -> new TableCell<>() {

            private final Button showButton = new Button("Ver");
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox buttons = new HBox(showButton, editButton, deleteButton);


            {
                // Asigna la clase CSS para centrar los botones
                buttons.getStyleClass().add("centered-buttons");
                showButton.getStyleClass().add("buttonA");
                editButton.getStyleClass().add("buttonA");
                deleteButton.getStyleClass().add("buttonB");


                showButton.setOnAction(actionEvent -> {

                            ProductDTO productDTO = getTableView().getItems().get(getIndex());
                            // Lógica para editar el usuarioalex
                            System.out.println("Editar producto: " + productDTO.getProductId());

                            ModalProduct modalProduct = new ModalProduct();
                            //Setea los campos del producto en el modal
                            modalProduct.textProductName.setText(productDTO.getProductName());
                            modalProduct.textBrand.setText(productDTO.getProductBrand());
                            modalProduct.textDescription.setText(productDTO.getProductDescription());
                            modalProduct.textPrice.setText(String.valueOf(productDTO.getUnitPrice()));
                            modalProduct.cbProductCategory.setValue(productDTO.getProductCategoryName());
                            modalProduct.cbSupplier.setValue(productDTO.getSupplierName());
                            modalProduct.showImageInImageView(productDTO.getProductImage());

                            // Configurar el modal
                            modalProduct.configureModal("Producto",
                                    "Visualiza todos los campos del producto.",
                                    "Cerrar",
                                    e -> {
                                        modalProduct.close();
                                    }
                            );

                            modalProduct.showModal(root);


                        }
                );

                editButton.setOnAction(event -> {

                    ProductDTO productDTO = getTableView().getItems().get(getIndex());
                    // Lógica para editar el usuarioalex
                    System.out.println("Editar producto: " + productDTO.getProductId());


                    ModalProduct modalProduct = new ModalProduct();
                    //Setea los campos del producto en el modal
                    modalProduct.textProductName.setText(productDTO.getProductName());
                    modalProduct.textBrand.setText(productDTO.getProductBrand());
                    modalProduct.textDescription.setText(productDTO.getProductDescription());
                    modalProduct.textPrice.setText(String.valueOf(productDTO.getUnitPrice()));
                    modalProduct.cbProductCategory.setValue(productDTO.getProductCategoryName());
                    modalProduct.cbSupplier.setValue(productDTO.getSupplierName());
                    modalProduct.showImageInImageView(productDTO.getProductImage());

                    // Configurar el modal
                    modalProduct.configureModal("Actualizar Producto",
                            "Modifica los campos necesarios para actualizar el producto.",
                            "Actualizar",
                            e -> {

                                String productName = modalProduct.textProductName.getText().trim();
                                String brand = modalProduct.textBrand.getText().trim();
                                String description = modalProduct.textDescription.getText().trim();
                                String price = modalProduct.textPrice.getText().trim();
                                Long productCategoryId = modalProduct.getValueSelectedProductCategory();
                                Long supplierId = modalProduct.getValueSelectedSupplier();
                                byte[] image = modalProduct.imageToByteArray(modalProduct.productImage.getImage());

                                List<String> errorMessages = new ArrayList<>();
                                boolean validationSuccessful = modalProduct.validateTextFieldsProduct(errorMessages);

                                if (validationSuccessful) {

                                    //Crea un DTO de producto
                                    ProductDTO updateProduct = new ProductDTO();
                                    updateProduct.setProductId(productDTO.getProductId());
                                    updateProduct.setProductName(productName);
                                    updateProduct.setProductBrand(brand);
                                    updateProduct.setProductDescription(description);
                                    updateProduct.setProductImage(image);
                                    updateProduct.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(price)));
                                    updateProduct.setAvailableStock(productDTO.getAvailableStock());
                                    updateProduct.setProductCategoryId(productCategoryId);
                                    updateProduct.setSupplierId(supplierId);
                                    productDAO.updateProduct(updateProduct);

                                    // Actualiza la tabla producto
                                    productDataLoadingUtil.loadProductTableData(productTable);

                                    //Crear una instancia del modal
                                    ModalDialog modalDialog = new ModalDialog();
                                    // Configurar el modal mediante un solo método
                                    modalDialog.configureModal(
                                            new Image("Images/iconCheck.png"),
                                            "Actualizado correctamente.",
                                            "Producto actualizado correctamente.",
                                            "OK",
                                            ev -> {
                                                // Lógica cuando se hace clic en el botón "Confirmar"
                                                System.out.println("Se hizo clic en Confirmar");
                                                modalDialog.close();
                                                modalProduct.close();
                                            }
                                    );

                                    modalDialog.showModal(root);

                                } else {

                                    // Mostrar mensajes de error si existen problemas de validación
                                    StringBuilder errorMessageBuilder = new StringBuilder();
                                    for (String errorMessage : errorMessages) {
                                        System.out.println(errorMessage);
                                        errorMessageBuilder.append("- ").append(errorMessage).append("\n");
                                    }

                                    String errorMessageText = errorMessageBuilder.toString();

                                    ModalDialog modalDialog = new ModalDialog();
                                    modalDialog.configureModal(
                                            new Image("Images/alert.png"),
                                            "Actualizacion incorrecta.",
                                            "No se pudo actualizar el producto correctamente: \n" + "\n" + errorMessageText,
                                            "OK",
                                            ev -> {
                                                System.out.println("Se hizo clic en Confirmar");
                                                modalDialog.close();
                                            }
                                    );

                                    modalDialog.showModal(root);
                                }


                            },
                            e -> {
                                // Lógica cuando se hace clic en el botón "Cancelar"
                                System.out.println("Se hizo clic en Cancelar");
                                modalProduct.close();
                            },
                            e -> {
                                //Obtiene la ruta de la imagen
                                modalProduct.getPathImage();
                            }
                    );


                    modalProduct.showModal(root);


                });


                deleteButton.setOnAction(event -> {

                    ProductDTO productDTO = getTableView().getItems().get(getIndex());
                    // Lógica para eliminar el usuario
                    System.out.println("Eliminar usuario: " + productDTO.getProductId());
                    productDAO.deleteProduct(productDTO.getProductId());
                    //Actualizar la tabla con el proveedor eliminado
                    productDataLoadingUtil.loadProductTableData(productTable);


                    //Crear una instancia del modal
                    ModalDialog modalDialog = new ModalDialog();
                    // Configurar el modal mediante un solo método
                    modalDialog.configureModal(new Image("Images/iconCheck.png"),
                            "Proveedor eliminado correctamente.",
                            "El producto con codigo :  " + productDTO.getProductId() + " ha sido eliminado correctamente",
                            "Ok",
                            ev -> {
                                modalDialog.close(); // Cierra el modal
                            });

                    modalDialog.showModal(root);


                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

    }




    private ScheduledExecutorService executor; // Para programar la búsqueda
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        productDAO = new ProductDAOHibernate(HibernateUtil.getSessionFactory());
        productCategoryDAO = new ProductCategoryDAOHibernate(HibernateUtil.getSessionFactory());

        configureTable();
        // Crear una instancia de DataLoadingUtil con UserDao inyectado
        productDataLoadingUtil = new ProductDataLoadingUtil(productDAO);
        // Carga los datos en la TableView al inicializar la ventana
        productDataLoadingUtil.loadProductTableData(productTable);


        // Inicializa el ProductUtil y comienza la búsqueda de productos
        productUtil = new ProductUtil(textSearch, productTable);
        productUtil.startProductSearch();



        //        List<ProductDTO> productList = productDAO.getAllProducts();
//
//        if (productList != null) {
//            // Iterar
//            for (ProductDTO productDTO : productList) {
//                System.out.println("Datos de productos:");
//                System.out.println(productDTO.getProductCategoryId());
//                System.out.println(productDTO.getSupplierId());
//                System.out.println();
//            }
//        }

//        List<ProductDTO> productsByName = productDAO.searchProductsBySingleCriteria("lenovo");
//
//        // Ahora puedes iterar y trabajar con los productos encontrados
//        for (ProductDTO product : productsByName) {
//            System.out.println("Codigo producto: "+product.getProductId());
//            System.out.println("Product Name: " + product.getProductName());
//            // Agrega cualquier otra lógica que desees para trabajar con los productos encontrados
//        }


    }
}
