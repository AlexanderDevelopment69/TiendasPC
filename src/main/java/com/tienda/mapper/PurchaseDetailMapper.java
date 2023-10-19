package com.tienda.mapper;

import com.tienda.Dao.PurchaseDAO;
import com.tienda.Model.Product;
import com.tienda.Model.Purchase;
import com.tienda.Model.PurchaseDetail;
import com.tienda.Model.Role;
import com.tienda.dto.ProductDTO;
import com.tienda.dto.PurchaseDTO;
import com.tienda.dto.PurchaseDetailDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseDetailMapper {
    public static PurchaseDetailDTO toPurchaseDetailDTO(PurchaseDetail purchaseDetail) {
        PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO();

        purchaseDetailDTO.setPurchaseDetailId(purchaseDetail.getPurchaseDetailId());
        purchaseDetailDTO.setQuantityPurchased(purchaseDetail.getQuantityPurchased());
        purchaseDetailDTO.setUnitPriceAtPurchase(purchaseDetail.getUnitPriceAtPurchase());
        purchaseDetailDTO.setSubtotal(purchaseDetail.getSubtotal());

        // Mapear la relación con Purchase
        if (purchaseDetail.getPurchase() != null) {
            purchaseDetailDTO.setPurchaseId(purchaseDetail.getPurchase().getPurchaseId());
            // Puedes establecer otros campos de Purchase en PurchaseDetailDTO si es necesario
        }

        // Mapear la relación con Product
        if (purchaseDetail.getProduct() != null) {
            purchaseDetailDTO.setProductId(purchaseDetail.getProduct().getProductId());
            purchaseDetailDTO.setProductName(purchaseDetail.getProduct().getProductName());
            purchaseDetailDTO.setProductBrand(purchaseDetail.getProduct().getProductBrand());
            purchaseDetailDTO.setProductDescription(purchaseDetail.getProduct().getProductDescription());
            // Puedes establecer otros campos de Product en PurchaseDetailDTO si es necesario
        }

        return purchaseDetailDTO;
    }


    public static PurchaseDetail toPurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) {
        PurchaseDetail purchaseDetail = new PurchaseDetail();

        // Copiar los campos de PurchaseDetailDTO a PurchaseDetail
        purchaseDetail.setPurchaseDetailId(purchaseDetailDTO.getPurchaseDetailId());
        purchaseDetail.setQuantityPurchased(purchaseDetailDTO.getQuantityPurchased());
        purchaseDetail.setUnitPriceAtPurchase(purchaseDetailDTO.getUnitPriceAtPurchase());
        purchaseDetail.setSubtotal(purchaseDetailDTO.getSubtotal());


        // Asigna el purchaseId del PurchaseDetailDTO
        if (purchaseDetailDTO.getPurchaseId() != null) {
            Purchase purchase = new Purchase();
            purchase.setPurchaseId(purchaseDetailDTO.getPurchaseId());
            purchaseDetail.setPurchase(purchase);
        }

        // Asigna el productId del PurchaseDetailDTO
        if (purchaseDetailDTO.getProductId() != null) {
            Product product= new Product();
            product.setProductId(purchaseDetailDTO.getProductId());
            purchaseDetail.setProduct(product);
        }

        return purchaseDetail;
    }




    public static List<PurchaseDetailDTO> toPurchaseDetailDTOList(List<PurchaseDetail> purchaseDetails) {
        return purchaseDetails.stream()
                .map(PurchaseDetailMapper::toPurchaseDetailDTO)
                .collect(Collectors.toList());
    }
}
