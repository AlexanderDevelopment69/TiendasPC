package com.tienda.Dao;

import com.tienda.dto.CustomerDTO;

import java.util.List;

public interface CustomerDAO {
    CustomerDTO getCustomerById(Long customerId);

    List<CustomerDTO> getAllCustomers();

    void saveCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    boolean isDniExists(String dni);

    boolean isRucExists(String ruc);

    boolean  isEmailExists(String email);

    List<CustomerDTO> searchCustomersBySingleCriteria(String criteria);

    List<CustomerDTO> searchCustomersByDniOrRuc(String criteria);

    CustomerDTO getCustomerByDniOrRuc(String dniOrRuc);
}

