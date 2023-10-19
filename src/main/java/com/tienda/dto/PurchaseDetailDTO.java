package com.tienda.dto;

import com.tienda.Model.Product;
import com.tienda.Model.Purchase;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseDetailDTO {
    private Long purchaseDetailId;
    private int quantityPurchased;
    private BigDecimal unitPriceAtPurchase;
    private BigDecimal subtotal;

//    private Purchase purchase;
//    private Product product;

    //Purchase
    private Long purchaseId;
    //Product
    private Long productId;
//
    private String productName;
    private String productBrand;
    private String productDescription;
}
