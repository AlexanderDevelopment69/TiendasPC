package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "product_categories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name",  nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> products;
}
