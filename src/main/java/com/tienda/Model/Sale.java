package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "sale_date_time")
    private Date saleDateTime;

    @Column(name = "subtotal", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal subtotal;

    @Column(name = "discount_total", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal discountTotal;

    @Column(name = "total", precision = 10, scale = 2) // precision = total de dígitos, scale = decimales
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleDetail> saleDetails;
}
