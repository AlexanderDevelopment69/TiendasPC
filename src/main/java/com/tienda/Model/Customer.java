package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "first_name")
    private String customerFirstName;
    @Column(name = "last_name")
    private String customerLastName;

    @Column(name = "dni", unique = true)
    private String customerDni;

    @Column(name = "ruc", unique = true)
    private String customerRuc;


    @Column(name = "address")
    private String customerAddress;

    @Column(name = "phone_number")
    private String customerPhoneNumber;

    @Column(name = "email")
    private String customerEmail;
}
