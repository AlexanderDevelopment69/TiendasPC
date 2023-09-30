package com.tienda.dto;


import lombok.Data;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private byte[] productImage; // Nuevo campo para la imagen del producto
    private double unitPrice;
    private int availableStock;


    private Long productCategoryId;
    private String productCategoryName;
    private Long supplierId;
    private String supplierName;

    // Getters y setters para los nuevos campos
}
