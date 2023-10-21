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

    private SaleDTO sale;             // Objeto DTO de la venta asociada a este detalle
    private ProductDTO product;       // Objeto DTO del producto asociado a este detalle

}
