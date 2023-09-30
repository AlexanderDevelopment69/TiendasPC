package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_detail_id")
    private Long purchaseDetailId;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity_purchased")
    private int quantityPurchased;

    @Column(name = "unit_price_at_purchase")
    private double unitPriceAtPurchase;

    @Column(name = "subtotal")
    private double subtotal;
}
