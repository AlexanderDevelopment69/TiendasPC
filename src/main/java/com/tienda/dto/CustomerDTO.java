package com.tienda.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerDni;
    private String customerRuc;
    private String customerAddress;
    private String customerPhoneNumber;
    private String customerEmail;
}
