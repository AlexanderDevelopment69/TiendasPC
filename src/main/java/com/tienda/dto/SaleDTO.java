package com.tienda.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SaleDTO {

    private Long saleId;               // ID de la venta
    private Date saleDateTime;         // Fecha y hora de la venta
    private BigDecimal subtotal;           // Subtotal de la venta
    private BigDecimal discountTotal;     // Descuento total aplicado
    private BigDecimal total;              // Monto total de la venta

    private List<SaleDetailDTO> saleDetails;  // Detalles de la venta.

    private CustomerDTO customer;
    private UserDTO user;




}



