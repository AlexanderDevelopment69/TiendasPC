package com.tienda.ControllerGUI.Product;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.ControllerGUI.Components.ModalProduct;
import com.tienda.ControllerGUI.Components.ModalProductCategory;
import com.tienda.Dao.*;
import com.tienda.Utils.ProductDataLoadingUtil;
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

import java.net.URL;
import java.util.ResourceBundle;

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
    private MFXTextField txtfSearch;

    ProductDAO productDAO;
    ProductCategoryDAO productCategoryDAO;

    ProductDataLoadingUtil productDataLoadingUtil;

    @FXML
    void handleAddProduct(ActionEvent event) {
        //Crear una instancia del modal
        ModalProduct modalProduct = new ModalProduct();
        // Configurar el modal mediante un solo método
        modalProduct.configureModal("Nuevo producto",
                "Completa todos los campos para registrar un nuevo producto.",
                "Registrar",
                ev -> {

                    if (!modalProduct.getTxtProduct().isEmpty()
                            && !modalProduct.getTxtBrand().isEmpty()
                            && !modalProduct.getTxtDescription().isEmpty()
                            && !modalProduct.getTxtPrice().isEmpty()
                            && modalProduct.validateComboBox()) {

                        ProductDTO newProductDTO = new ProductDTO();
                        newProductDTO.setProductName(modalProduct.getTxtProduct());
                        newProductDTO.setProductBrand(modalProduct.getTxtBrand());
                        newProductDTO.setProductDescription(modalProduct.getTxtDescription());
                        newProductDTO.setUnitPrice(Double.parseDouble(modalProduct.getTxtPrice()));
                        newProductDTO.setAvailableStock(0);
                        newProductDTO.setProductCategoryId(modalProduct.getValueSelectedProductCategory());
                        newProductDTO.setSupplierId(modalProduct.getValueSelectedSupplier());
                        newProductDTO.setProductImage(modalProduct.getImageToByteArray());
                        productDAO.saveProduct(newProductDTO);
                        handleLoadDate();

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

                        //Crear una instancia del modal
                        ModalDialog modalDialog = new ModalDialog();
                        // Configurar el modal mediante un solo método
                        modalDialog.configureModal(
                                new Image("Images/alert.png"),
                                "Registro incorrecto.",
                                "Completa todos los campos del producto, la imagen es opcional.",
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
                    modalProduct.close(); // Cierra el modal
                },
                ev -> {
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
                            modalProduct.setTxtProduct(productDTO.getProductName());
                            modalProduct.setTxtBrand(productDTO.getProductBrand());
                            modalProduct.setTxtDescription(productDTO.getProductDescription());
                            modalProduct.setTxtPrice(String.valueOf(productDTO.getUnitPrice()));
                            modalProduct.setValueCbProductCategory(productDTO.getProductCategoryName());
                            modalProduct.setValueCbSupplier(productDTO.getSupplierName());
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
                    modalProduct.setTxtProduct(productDTO.getProductName());
                    modalProduct.setTxtBrand(productDTO.getProductBrand());
                    modalProduct.setTxtDescription(productDTO.getProductDescription());
                    modalProduct.setTxtPrice(String.valueOf(productDTO.getUnitPrice()));
                    modalProduct.setValueCbProductCategory(productDTO.getProductCategoryName());
                    modalProduct.setValueCbSupplier(productDTO.getSupplierName());
                    modalProduct.showImageInImageView(productDTO.getProductImage());
                    // Configurar el modal
                    modalProduct.configureModal("Actualizar Producto",
                            "Modifica los campos necesarios para actualizar el producto.",
                            "Actualizar",
                            e -> {
                                //Crea un DTO de producto
                                ProductDTO updateProduct = new ProductDTO();

                                updateProduct.setProductId(productDTO.getProductId());
                                updateProduct.setProductName(modalProduct.getTxtProduct());
                                updateProduct.setProductBrand(modalProduct.getTxtBrand());
                                updateProduct.setProductDescription(modalProduct.getTxtDescription());
                                updateProduct.setProductImage(modalProduct.getImageToByteArray());
                                updateProduct.setUnitPrice(Double.parseDouble(modalProduct.getTxtPrice()));
                                updateProduct.setProductCategoryId(modalProduct.getValueSelectedProductCategory());
                                updateProduct.setSupplierId(modalProduct.getValueSelectedSupplier());
                                productDAO.updateProduct(updateProduct);

                                // Carga los datos en la TableView al inicializar la ventana
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
                    handleLoadDate();


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

    }
}
