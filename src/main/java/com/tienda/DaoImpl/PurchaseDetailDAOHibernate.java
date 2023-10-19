package com.tienda.DaoImpl;

import com.tienda.Dao.PurchaseDetailDAO;
import com.tienda.dto.PurchaseDetailDTO;
import com.tienda.mapper.PurchaseDetailMapper;
import com.tienda.Model.PurchaseDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PurchaseDetailDAOHibernate implements PurchaseDetailDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public PurchaseDetailDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene un detalle de compra por su ID.
     *
     * @param purchaseDetailId El ID del detalle de compra que se desea obtener.
     * @return El DTO del detalle de compra si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public PurchaseDetailDTO getPurchaseDetailById(Long purchaseDetailId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        PurchaseDetailDTO purchaseDetailDTO = null; // Inicializa un DTO de detalle de compra

        try {
            // Obtiene el detalle de compra por su ID
            PurchaseDetail purchaseDetail = session.get(PurchaseDetail.class, purchaseDetailId);
            // Convierte la entidad en un DTO
            purchaseDetailDTO = PurchaseDetailMapper.toPurchaseDetailDTO(purchaseDetail);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return purchaseDetailDTO; // Devuelve el DTO de detalle de compra
    }

    /**
     * Obtiene todos los detalles de compra.
     *
     * @return Una lista de DTOs de detalles de compra.
     */
    @Override
    public List<PurchaseDetailDTO> getAllPurchaseDetails() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<PurchaseDetailDTO> purchaseDetailDTOs = null; // Inicializa una lista de DTOs de detalles de compra

        try {
            // Consulta todos los detalles de compra en la base de datos
            Query<PurchaseDetail> query = session.createQuery("FROM PurchaseDetail", PurchaseDetail.class);
            // Obtiene la lista de detalles de compra
            List<PurchaseDetail> purchaseDetails = query.list();
            // Convierte las entidades en DTOs
            purchaseDetailDTOs = PurchaseDetailMapper.toPurchaseDetailDTOList(purchaseDetails);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return purchaseDetailDTOs; // Devuelve la lista de DTOs de detalles de compra
    }

    /**
     * Guarda un nuevo detalle de compra en la base de datos.
     *
     * @param purchaseDetailDTO El DTO del detalle de compra a ser guardado.
     */
    @Override
    public void savePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Convierte el DTO en una entidad
            PurchaseDetail purchaseDetail = PurchaseDetailMapper.toPurchaseDetail(purchaseDetailDTO);
            session.save(purchaseDetail); // Guarda el detalle de compra en la base de datos
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
     * Actualiza un detalle de compra existente en la base de datos.
     *
     * @param purchaseDetailDTO El DTO del detalle de compra a ser actualizado.
     */
    @Override
    public void updatePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Convierte el DTO en una entidad
            PurchaseDetail purchaseDetail = PurchaseDetailMapper.toPurchaseDetail(purchaseDetailDTO);
            session.update(purchaseDetail); // Actualiza el detalle de compra en la base de datos
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
     * Elimina un detalle de compra por su ID desde la base de datos.
     *
     * @param purchaseDetailId El ID del detalle de compra que se desea eliminar.
     */
    @Override
    public void deletePurchaseDetail(Long purchaseDetailId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Obtiene el detalle de compra por su ID
            PurchaseDetail purchaseDetail = session.get(PurchaseDetail.class, purchaseDetailId);
            if (purchaseDetail != null) {
                session.delete(purchaseDetail); // Elimina el detalle de compra de la base de datos
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


}
