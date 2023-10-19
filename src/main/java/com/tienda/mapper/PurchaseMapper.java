package com.tienda.mapper;


import com.tienda.Model.Purchase;
import com.tienda.dto.PurchaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseMapper {
    public static PurchaseDTO toPurchaseDTO(Purchase purchase) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseId(purchase.getPurchaseId());
        purchaseDTO.setPurchaseDateTime(purchase.getPurchaseDateTime());
        purchaseDTO.setTotalPurchase(purchase.getTotalPurchase());
//        purchaseDTO.setPurchaseDetails(
//                purchase.getPurchaseDetails().stream()
//                        .map(PurchaseDetailMapper::toPurchaseDetailDTO)
//                        .collect(Collectors.toList())
//        );
        purchaseDTO.setSupplier(SupplierMapper.toSupplierDTO(purchase.getSupplier()));
        purchaseDTO.setUser(UserMapper.toUserDTO(purchase.getUser()));
        return purchaseDTO;
    }

    public static Purchase toPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseDTO.getPurchaseId());
        purchase.setPurchaseDateTime(purchaseDTO.getPurchaseDateTime());
        purchase.setSupplier(SupplierMapper.toSupplier(purchaseDTO.getSupplier()));
        purchase.setUser(UserMapper.toUser(purchaseDTO.getUser()));
        purchase.setTotalPurchase(purchaseDTO.getTotalPurchase());
        return purchase;
    }

    /**
     * Convierte una lista de entidades Purchase en una lista de DTOs PurchaseDTO.
     *
     * @param purchases La lista de entidades Purchase a ser convertida.
     * @return Una lista de DTOs PurchaseDTO que representan las compras.
     */
    public static List<PurchaseDTO> toPurchaseDTOList(List<Purchase> purchases) {
        return purchases.stream()
                .map(PurchaseMapper::toPurchaseDTO) // Mapea cada entidad Purchase a un DTO PurchaseDTO
                .collect(Collectors.toList()); // Recopila los resultados en una lista y la devuelve
    }

}
