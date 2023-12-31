package com.tienda.Dao;

import com.tienda.dto.SupplierDTO;
import com.tienda.dto.UserDTO;

import java.util.List;

public interface SupplierDAO {
    SupplierDTO getSupplierById(Long supplierId);

    List<SupplierDTO> getAllSuppliers();

    void saveSupplier(SupplierDTO supplierDTO);

    void updateSupplier(SupplierDTO supplierDTO);

    void deleteSupplier(Long supplierId);


    boolean existsSupplierWithRuc(String ruc);

    boolean existsSupplierWithEmail(String email);

    List<SupplierDTO> searchSuppliersBySingleCriteria(String criteria);

    SupplierDTO getSupplierBySupplierNameOrRuc(String supplierName);


}
