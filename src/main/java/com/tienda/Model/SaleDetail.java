package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "sale_details")
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "quantity_sold")
    private int quantitySold;

    @Column(name = "discount_per_product", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal discountPerProduct;

    @Column(name = "subtotal_per_product", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal subtotalPerProduct;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
