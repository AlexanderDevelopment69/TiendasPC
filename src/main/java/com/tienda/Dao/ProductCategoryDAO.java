package com.tienda.Dao;

import com.tienda.dto.ProductCategoryDTO;

import java.util.List;

public interface ProductCategoryDAO {
    ProductCategoryDTO getCategoryById(Long categoryId);
    List<ProductCategoryDTO> getAllCategories();
    void saveCategory(ProductCategoryDTO categoryDTO);
    void updateCategory(ProductCategoryDTO productDTO);
    void deleteCategory(Long categoryId);

}
