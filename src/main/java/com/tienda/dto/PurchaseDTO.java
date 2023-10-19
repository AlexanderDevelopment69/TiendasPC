package com.tienda.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class PurchaseDTO {

    private Long purchaseId;  // Identificador único de la compra.
    private Date purchaseDateTime;  // Fecha y hora de la compra.
    private BigDecimal totalPurchase;  // Total de la compra.
    private List<PurchaseDetailDTO> purchaseDetails;  // Detalles de la compra.

    private SupplierDTO supplier;  // Proveedor asociado a la compra.
    private UserDTO user;  // Usuario que realizó la compra.


    // Getters y setters para acceder y modificar los atributos de la compra.
}
