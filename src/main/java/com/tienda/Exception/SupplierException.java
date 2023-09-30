package com.tienda.Exception;


public class SupplierException extends Exception {
    public SupplierException(String message) {
        super(message);
    }

    public static SupplierException duplicateRUC(String ruc) {
        return new SupplierException("El proveedor ya está registrado con este RUC: " + ruc);
    }
    public static SupplierException duplicateEmail(String email) {
        return new SupplierException("El proveedor ya está registrado con este email: " + email);
    }
    // Otros constructores según necesidad
}
