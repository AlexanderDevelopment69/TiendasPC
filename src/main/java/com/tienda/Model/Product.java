package com.tienda.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long product_id;
    @Column(name = "product_name", nullable = false)
    private String product_name;
    @Column(name = "product_price", nullable = false)
    private Long product_price;
    @Column(name = "product_cost", nullable = false)
    private Long product_cost;

}