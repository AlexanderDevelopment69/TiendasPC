package com.tienda.Dao;

import com.tienda.dto.SaleDTO;
import com.tienda.dto.SaleDetailDTO;

import java.util.List;

public interface SaleDAO {
    SaleDTO getSaleById(Long saleId);
    List<SaleDTO> getAllSales();
    void saveSale(SaleDTO saleDTO);
    void updateSale(SaleDTO saleDTO);
    void deleteSale(Long saleId);
    Long getLastSaleId();
    List<SaleDetailDTO> getSaleDetailsBySaleId(Long saleId);

}
