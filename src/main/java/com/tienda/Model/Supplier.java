package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "supplier_name", nullable = false)  // Nombre del proveedor no puede ser nulo
    private String supplierName;

    @Column(name = "supplier_ruc", nullable = false, unique = true)  // RUC debe ser único
    private String  supplierRuc;

    @Column(name = "supplier_address", nullable = false)  // Dirección del proveedor no puede ser nula
    private String supplierAddress;

    @Column(name = "supplier_phone_number", nullable = false)  // Número de teléfono no puede ser nulo
    private String supplierPhoneNumber;

    @Column(name = "supplier_email", nullable = false, unique = true)  // Correo electrónico no puede ser nulo
    private String supplierEmail;

    // Getters y setters
}

