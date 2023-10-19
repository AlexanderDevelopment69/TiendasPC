package com.tienda.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleDetailDTO {

    private Long detailId;             // ID del detalle
    private int quantitySold;         // Cantidad vendida
    private BigDecimal discountPerProduct; // Descuento por producto
    private BigDecimal subtotalPerProduct; // Subtotal por producto

    //Nuevo campo agreado solo para agregar campos a la tabla
    private BigDecimal total;

//    //VENTA
//    private Long saleId;// ID de la venta asociada a este detalle
//
//    //PRODUCTO
//    private Long productId;           // ID del producto asociado a este detalle
//    private String productName;
//    private String productBrand;
//    private String productDescription;
//    private BigDecimal unitPrice;

    private SaleDTO sale;             // Objeto DTO de la venta asociada a este detalle
    private ProductDTO product;       // Objeto DTO del producto asociado a este detalle

}
