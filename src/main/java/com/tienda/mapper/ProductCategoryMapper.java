package com.tienda.mapper;

import com.tienda.Model.ProductCategory;
import com.tienda.dto.ProductCategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryMapper {

    public static ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory) {
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        productCategoryDTO.setCategoryId(productCategory.getCategoryId());
        productCategoryDTO.setCategoryName(productCategory.getCategoryName());
        return productCategoryDTO;
    }

    public static ProductCategory toProductCategory(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(productCategoryDTO.getCategoryId());
        productCategory.setCategoryName(productCategoryDTO.getCategoryName());
        return productCategory;
    }

    public static List<ProductCategoryDTO> toProductCategoryDTOList(List<ProductCategory> productCategories) {
        return productCategories.stream()
                .map(ProductCategoryMapper::toProductCategoryDTO)
                .collect(Collectors.toList());
    }
}
