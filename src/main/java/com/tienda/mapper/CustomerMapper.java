package com.tienda.mapper;

import com.tienda.Model.Customer;
import com.tienda.dto.CustomerDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {
    public static CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setCustomerFirstName(customer.getCustomerFirstName());
        customerDTO.setCustomerLastName(customer.getCustomerLastName());
        customerDTO.setCustomerDni(customer.getCustomerDni());
        customerDTO.setCustomerRuc(customer.getCustomerRuc());
        customerDTO.setCustomerAddress(customer.getCustomerAddress());
        customerDTO.setCustomerPhoneNumber(customer.getCustomerPhoneNumber());
        customerDTO.setCustomerEmail(customer.getCustomerEmail());

        return customerDTO;
    }

    public static Customer toCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }

        Customer customer = new Customer();

        // Usar la función nullIfEmpty para manejar valores nulos o vacíos
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setCustomerFirstName(nullIfEmpty(customerDTO.getCustomerFirstName()));
        customer.setCustomerLastName(nullIfEmpty(customerDTO.getCustomerLastName()));
        customer.setCustomerDni(nullIfEmpty(customerDTO.getCustomerDni()));
        customer.setCustomerRuc(nullIfEmpty(customerDTO.getCustomerRuc()));
        customer.setCustomerAddress(nullIfEmpty(customerDTO.getCustomerAddress()));
        customer.setCustomerPhoneNumber(nullIfEmpty(customerDTO.getCustomerPhoneNumber()));
        customer.setCustomerEmail(nullIfEmpty(customerDTO.getCustomerEmail()));

        return customer;
    }


    public static String nullIfEmpty(String value) {
        // Verificar si el valor es nulo o si es una cadena vacía después de quitar espacios en blanco
        // Si el valor es nulo o una cadena vacía, se devuelve null; de lo contrario, se devuelve el valor original
        return (value == null || value.trim().isEmpty()) ? null : value;
    }



    public static List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toCustomerDTO)
                .collect(Collectors.toList());
    }
}
