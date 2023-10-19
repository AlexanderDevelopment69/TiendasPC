package com.tienda.DaoImpl;

import com.tienda.Dao.DocumentDAO;
import com.tienda.Model.Customer;
import com.tienda.Model.Document;
import com.tienda.dto.DocumentDTO;
import com.tienda.mapper.DocumentMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentDAOHibernate implements DocumentDAO {

    private final SessionFactory sessionFactory; // Factoría de sesiones de Hibernate

    // Constructor que recibe la factoría de sesiones como parámetro
    public DocumentDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene un documento por su ID.
     *
     * @param documentId El ID del documento que se desea obtener.
     * @return El DTO del documento correspondiente al ID proporcionado, o null si no se encuentra.
     */
    @Override
    public DocumentDTO getDocumentById(Long documentId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            Document document = session.get(Document.class, documentId); // Obtiene el documento por su ID
            return DocumentMapper.toDocumentDTO(document); // Convierte la entidad en un DTO y lo devuelve
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
        return null; // Retorna null en caso de error o si el documento no se encuentra
    }



    /**
     * Obtiene una lista de todos los documentos almacenados en la base de datos.
     *
     * @return Una lista de DTOs de documentos.
     */
    @Override
    public List<DocumentDTO> getAllDocuments() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<DocumentDTO> documentDTOs = new ArrayList<>(); // Inicializa una lista de DTOs de documentos

        try {
            Query<Document> query = session.createQuery("FROM Document", Document.class); // Crea una consulta HQL para obtener todos los documentos
            List<Document> documents = query.list(); // Obtiene la lista de documentos
            documentDTOs = DocumentMapper.toDocumentDTOList(documents); // Ejecuta la consulta y convierte los resultados en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return documentDTOs; // Devuelve la lista de DTOs de documentos o una lista vacía en caso de error.
    }


    /**
     * Guarda un nuevo documento en la base de datos.
     *
     * @param documentDTO El DTO del documento a ser guardado.
     */
    @Override
    public void saveDocument(DocumentDTO documentDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Document document = DocumentMapper.toDocument(documentDTO); // Convierte el DTO en una entidad Document
            session.save(document); // Guarda el documento en la base de datos
            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    /**
     * Actualiza un documento en la base de datos.
     *
     * @param documentDTO El DTO del documento con los datos actualizados.
     */
    @Override
    public void updateDocument(DocumentDTO documentDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Document document = DocumentMapper.toDocument(documentDTO); // Convierte el DTO en una entidad Document

            session.update(document); // Actualiza el documento en la base de datos

            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }




    /**
     * Elimina un documento de la base de datos por su ID.
     *
     * @param documentId El ID del documento que se va a eliminar.
     */
    @Override
    public void deleteDocument(Long documentId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Document document = session.get(Document.class, documentId); // Obtiene el documento por su ID

            if (document != null) {
                session.delete(document); // Elimina el documento de la base de datos
            }

            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    /**
     * Obtiene una lista de documentos por el ID de tipo de documento.
     *
     * @param documentTypeId El ID del tipo de documento para el cual se desean obtener los documentos.
     * @return Una lista de DTOs de documentos relacionados con el tipo de documento especificado.
     */
    @Override
    public List<DocumentDTO> getDocumentsByType(Long documentTypeId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<DocumentDTO> documentDTOs = new ArrayList<>(); // Inicializa una lista de DTOs de documentos

        try {
            Query<Document> query = session.createQuery("FROM Document WHERE documentType.documentTypeId = :documentTypeId", Document.class);
            query.setParameter("documentTypeId", documentTypeId);

            List<Document> documents = query.list(); // Obtiene la lista de documentos por el ID del tipo de documento
            documentDTOs = DocumentMapper.toDocumentDTOList(documents); // Convierte las entidades en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return documentDTOs; // Devuelve la lista de DTOs de documentos relacionados con el tipo de documento.
    }


    /**
     * Obtiene un documento por su número de documento.
     *
     * @param documentNumber El número de documento para el cual se desea obtener el documento.
     * @return Un DTO de documento relacionado con el número de documento especificado.
     */
    @Override
    public DocumentDTO getDocumentByDocumentNumber(String documentNumber) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate

        try {
            Query<Document> query = session.createQuery("FROM Document WHERE documentNumber = :documentNumber", Document.class);
            query.setParameter("documentNumber", documentNumber);
            query.setMaxResults(1); // Limita el resultado a un solo documento, ya que el número de documento debe ser único

            Document document = query.uniqueResult(); // Obtiene el documento único que coincide con el número de documento
            if (document != null) {
                return DocumentMapper.toDocumentDTO(document); // Convierte la entidad en un DTO y lo devuelve
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return null; // Devuelve null si no se encuentra ningún documento con el número especificado.
    }


    /**
     * Obtiene una lista de documentos por el ID de cliente.
     *
     * @param customerId El ID del cliente para el cual se desean obtener los documentos.
     * @return Una lista de DTOs de documentos relacionados con el cliente especificado.
     */
    @Override
    public List<DocumentDTO> getDocumentsByCustomer(Long customerId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<DocumentDTO> documentDTOs = new ArrayList<>(); // Inicializa una lista de DTOs de documentos

        try {
            Query<Document> query = session.createQuery("FROM Document WHERE customer.customerId = :customerId", Document.class);
            query.setParameter("customerId", customerId);

            List<Document> documents = query.list(); // Obtiene la lista de documentos por el ID del cliente
            documentDTOs = DocumentMapper.toDocumentDTOList(documents); // Convierte las entidades en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return documentDTOs; // Devuelve la lista de DTOs de documentos relacionados con el cliente.
    }


    /**
     * Obtiene una lista de documentos por el ID de usuario.
     *
     * @param userId El ID del usuario para el cual se desean obtener los documentos.
     * @return Una lista de DTOs de documentos relacionados con el usuario especificado.
     */
    @Override
    public List<DocumentDTO> getDocumentsByUser(Long userId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<DocumentDTO> documentDTOs = new ArrayList<>(); // Inicializa una lista de DTOs de documentos

        try {
            Query<Document> query = session.createQuery("FROM Document WHERE user.userId = :userId", Document.class);
            query.setParameter("userId", userId);

            List<Document> documents = query.list(); // Obtiene la lista de documentos por el ID del usuario
            documentDTOs = DocumentMapper.toDocumentDTOList(documents); // Convierte las entidades en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return documentDTOs; // Devuelve la lista de DTOs de documentos relacionados con el usuario.
    }

}
