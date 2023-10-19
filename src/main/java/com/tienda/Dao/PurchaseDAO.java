package com.tienda.Dao;

import com.tienda.dto.PurchaseDTO;
import com.tienda.dto.PurchaseDetailDTO;

import java.util.List;

public interface PurchaseDAO {
    PurchaseDTO getPurchaseById(Long purchaseId);
    List<PurchaseDTO> getAllPurchases();
    void savePurchase(PurchaseDTO purchaseDTO);
    void updatePurchase(PurchaseDTO purchaseDTO);
    void deletePurchase(Long purchaseId);

    List<PurchaseDetailDTO> getPurchaseDetailsByPurchaseId(Long purchaseId);
    Long getLastPurchaseId();
}
