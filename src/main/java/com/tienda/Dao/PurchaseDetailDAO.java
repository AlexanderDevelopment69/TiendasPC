package com.tienda.Dao;

import com.tienda.dto.PurchaseDetailDTO;

import java.util.List;

public interface PurchaseDetailDAO {
    PurchaseDetailDTO getPurchaseDetailById(Long purchaseDetailId);

    List<PurchaseDetailDTO> getAllPurchaseDetails();

    void savePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO);

    void updatePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO);

    void deletePurchaseDetail(Long purchaseDetailId);

}
