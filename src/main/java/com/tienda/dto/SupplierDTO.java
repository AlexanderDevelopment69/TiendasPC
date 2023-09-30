package com.tienda.dto;

import lombok.Data;

@Data
public class SupplierDTO {
    private Long supplierId; // ID del proveedor (clave primaria)
    private String supplierName; // Nombre del proveedor
    private String supplierRuc; // RUC (Registro Único de Contribuyentes)
    private String supplierAddress; // Dirección del proveedor
    private String supplierPhoneNumber; // Número de teléfono del proveedor
    private String supplierEmail; // Correo electrónico del proveedor

    // Getters y setters
}
