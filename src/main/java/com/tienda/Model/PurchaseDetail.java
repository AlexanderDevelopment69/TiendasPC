package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_detail_id")
    private Long purchaseDetailId;

    @Column(name = "quantity_purchased")
    private int quantityPurchased;

    @Column(name = "unit_price_at_purchase", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal unitPriceAtPurchase;

    @Column(name = "subtotal", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
