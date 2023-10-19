package com.tienda.mapper;

import com.tienda.Model.Document;
import com.tienda.dto.DocumentDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentMapper {
    public static DocumentDTO toDocumentDTO(Document document) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDocumentId(document.getDocumentId());
        documentDTO.setDocumentNumber(document.getDocumentNumber());
        documentDTO.setDocumentType(DocumentTypeMapper.toDocumentTypeDTO(document.getDocumentType()));
        documentDTO.setIssueDate(document.getIssueDate());
        documentDTO.setSale(SaleMapper.toSaleDTO(document.getSale()));
        documentDTO.setCustomer(CustomerMapper.toCustomerDTO(document.getCustomer()));
        documentDTO.setUser(UserMapper.toUserDTO(document.getUser()));
        documentDTO.setSubtotal(document.getSubtotal());
        documentDTO.setTotalDiscount(document.getTotalDiscount());
        documentDTO.setIgvAmount(document.getIgvAmount());
        documentDTO.setTotalAmount(document.getTotalAmount());
        return documentDTO;
    }

    public static Document toDocument(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setDocumentId(documentDTO.getDocumentId());
        document.setDocumentNumber(documentDTO.getDocumentNumber());
        document.setDocumentType(DocumentTypeMapper.toDocumentType(documentDTO.getDocumentType()));
        document.setIssueDate(documentDTO.getIssueDate());
        document.setSale(SaleMapper.toSale(documentDTO.getSale()));
        document.setCustomer(CustomerMapper.toCustomer(documentDTO.getCustomer()));
        document.setUser(UserMapper.toUser(documentDTO.getUser()));
        document.setSubtotal(documentDTO.getSubtotal());
        document.setTotalDiscount(documentDTO.getTotalDiscount());
        document.setIgvAmount(documentDTO.getIgvAmount());
        document.setTotalAmount(documentDTO.getTotalAmount());
        return document;
    }

    public static List<DocumentDTO> toDocumentDTOList(List<Document> documents) {
        return documents.stream()
                .map(DocumentMapper::toDocumentDTO)
                .collect(Collectors.toList());
    }
}
