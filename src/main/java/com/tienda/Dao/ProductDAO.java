package com.tienda.Dao;

import com.tienda.dto.ProductDTO;

import java.util.List;

public interface ProductDAO {
    void saveProduct(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts();

    void deleteProduct(Long productId);

    List<ProductDTO> searchProductsBySingleCriteria(String criteria);


    // Nuevo método para obtener todos los productos por proveedor
    List<ProductDTO> getProductsBySupplier(Long supplierId);

    // Nuevo método para actualizar el stock de un producto
    void updateProductStock(Long productId, int quantityToUpdate);

    void updateProductStockOnSale(Long productId, int quantitySold);


    ProductDTO getProductByProductNameOrCategoryOrId(String searchCriteria);


    List<ProductDTO> searchProductByProductNameOrCategoryOrId(String searchCriteria);
}
