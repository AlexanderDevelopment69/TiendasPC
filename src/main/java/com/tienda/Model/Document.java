package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_number", unique = true)
    private String documentNumber;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @Column(name = "issue_date")
    private Date issueDate;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total_discount", precision = 10, scale = 2)
    private BigDecimal totalDiscount;

    @Column(name = "igv_amount", precision = 10, scale = 2)
    private BigDecimal igvAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
}
