package com.tienda.Dao;

import com.tienda.dto.ProductDTO;

import java.util.List;

public interface ProductDAO {
    void saveProduct(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts();

    void deleteProduct(Long productId);
}
