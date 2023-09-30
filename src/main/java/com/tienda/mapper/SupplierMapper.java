package com.tienda.mapper;

import com.tienda.Model.Supplier;
import com.tienda.dto.SupplierDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierMapper {
    public static SupplierDTO toSupplierDTO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setSupplierId(supplier.getSupplierId());
        supplierDTO.setSupplierName(supplier.getSupplierName());
        supplierDTO.setSupplierRuc(supplier.getSupplierRuc());
        supplierDTO.setSupplierAddress(supplier.getSupplierAddress());
        supplierDTO.setSupplierPhoneNumber(supplier.getSupplierPhoneNumber());
        supplierDTO.setSupplierEmail(supplier.getSupplierEmail());

        return supplierDTO;
    }

    public static Supplier toSupplier(SupplierDTO supplierDTO) {
        if (supplierDTO == null) {
            return null;
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierDTO.getSupplierId());
        supplier.setSupplierName(supplierDTO.getSupplierName());
        supplier.setSupplierRuc(supplierDTO.getSupplierRuc());
        supplier.setSupplierAddress(supplierDTO.getSupplierAddress());
        supplier.setSupplierPhoneNumber(supplierDTO.getSupplierPhoneNumber());
        supplier.setSupplierEmail(supplierDTO.getSupplierEmail());

        return supplier;
    }

    public static List<SupplierDTO> toSupplierDTOList(List<Supplier> suppliers) {
        return suppliers.stream()
                .map(SupplierMapper::toSupplierDTO)
                .collect(Collectors.toList());
    }
}
