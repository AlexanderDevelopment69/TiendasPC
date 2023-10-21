package com.tienda.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class DocumentDTO {
    private Long documentId;
    private String documentNumber;
    private DocumentTypeDTO documentType; // Aquí, DocumentTypeDTO debe ser una clase DTO para representar el tipo de documento.
    private Date issueDate;
    private SaleDTO sale; // SaleDTO es una clase DTO que representa la venta asociada al documento.
    private CustomerDTO customer; // CustomerDTO representa el cliente asociado al documento.
    private UserDTO user; // UserDTO representa el usuario asociado al documento.
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal igvAmount;
    private BigDecimal totalAmount;

    // Agregar una lista de detalles de venta
    private List<SaleDetailDTO> saleDetails;

}
