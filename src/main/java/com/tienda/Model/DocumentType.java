package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "documentTypes")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "document_type_name")
    private String documentTypeName;
}
