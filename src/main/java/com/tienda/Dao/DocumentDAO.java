package com.tienda.Dao;

import com.tienda.dto.DocumentDTO;

import java.util.List;

public interface DocumentDAO {
    DocumentDTO getDocumentById(Long documentId);
    List<DocumentDTO> getAllDocuments();
    void saveDocument(DocumentDTO documentDTO);
    void updateDocument(DocumentDTO documentDTO);
    void deleteDocument(Long documentId);
    List<DocumentDTO> getDocumentsByType(Long documentTypeId);
    DocumentDTO getDocumentByDocumentNumber(String documentNumber);
    List<DocumentDTO> getDocumentsByCustomer(Long customerId);
    List<DocumentDTO> getDocumentsByUser(Long userId);

    DocumentDTO getDocumentBySaleId(Long saleId);
}
