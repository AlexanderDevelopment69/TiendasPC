package com.tienda.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private byte[] productImage; // Nuevo campo para la imagen del producto
    private BigDecimal unitPrice;
    private int availableStock;


    private Long productCategoryId;
    private String productCategoryName;
    private Long supplierId;
    private String supplierName;

    // Getters y setters para los nuevos campos
}
