package com.tienda.mapper;

import com.tienda.Model.DocumentType;
import com.tienda.dto.DocumentTypeDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentTypeMapper {
    public static DocumentTypeDTO toDocumentTypeDTO(DocumentType documentType) {
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setDocumentTypeId(documentType.getDocumentTypeId());
        documentTypeDTO.setDocumentTypeName(documentType.getDocumentTypeName());
        return documentTypeDTO;
    }

    public static DocumentType toDocumentType(DocumentTypeDTO documentTypeDTO) {
        DocumentType documentType = new DocumentType();
        documentType.setDocumentTypeId(documentTypeDTO.getDocumentTypeId());
        documentType.setDocumentTypeName(documentTypeDTO.getDocumentTypeName());
        return documentType;
    }

    public static List<DocumentTypeDTO> toDocumentTypeDTOList(List<DocumentType> documentTypes) {
        return documentTypes.stream()
                .map(DocumentTypeMapper::toDocumentTypeDTO)
                .collect(Collectors.toList());
    }
}
