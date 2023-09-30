package com.tienda.Model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name",nullable = false)
    private String productName;

    @Column(name = "product_brand",nullable = false)
    private String productBrand;

    @Column(name = "product_description")
    private String productDescription;

    @Lob // Esta anotación indica que se almacenará un objeto grande (en este caso, la imagen)
    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage; // Aquí se almacena la imagen como bytes

    @Column(name = "unit_price",nullable = false)
    private double unitPrice;

    @Column(name = "available_stock",nullable = false)
    private int availableStock;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id", nullable = false)
    private Supplier supplier;
}
