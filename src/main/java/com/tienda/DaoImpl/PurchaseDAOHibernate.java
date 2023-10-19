package com.tienda.DaoImpl;

import com.tienda.Dao.PurchaseDAO;
import com.tienda.Model.Product;
import com.tienda.Model.PurchaseDetail;
import com.tienda.dto.PurchaseDTO;
import com.tienda.dto.PurchaseDetailDTO;
import com.tienda.mapper.PurchaseDetailMapper;
import com.tienda.mapper.PurchaseMapper;
import com.tienda.Model.Purchase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.tienda.Dao.ProductDAO;

import java.util.List;

public class PurchaseDAOHibernate implements PurchaseDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public PurchaseDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene una compra por su ID.
     *
     * @param purchaseId El ID de la compra que se desea obtener.
     * @return El DTO de la compra si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public PurchaseDTO getPurchaseById(Long purchaseId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        PurchaseDTO purchaseDTO = null; // Inicializa un DTO de compra

        try {
            // Obtiene la compra por su ID
            Purchase purchase = session.get(Purchase.class, purchaseId);
            // Convierte la entidad en un DTO
            purchaseDTO = PurchaseMapper.toPurchaseDTO(purchase);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return purchaseDTO; // Devuelve el DTO de la compra
    }

    /**
     * Obtiene todas las compras.
     *
     * @return Una lista de DTOs de compras.
     */
    @Override
    public List<PurchaseDTO> getAllPurchases() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<PurchaseDTO> purchaseDTOs = null; // Inicializa una lista de DTOs de compras

        try {
            // Consulta todas las compras en la base de datos
            Query<Purchase> query = session.createQuery("FROM Purchase", Purchase.class);
            // Obtiene la lista de compras
            List<Purchase> purchases = query.list();
            // Convierte las entidades en DTOs
            purchaseDTOs = PurchaseMapper.toPurchaseDTOList(purchases);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return purchaseDTOs; // Devuelve la lista de DTOs de compras
    }

//    /**
//     * Guarda una nueva compra en la base de datos.
//     *
//     * @param purchaseDTO El DTO de la compra a ser guardada.
//     */
//    @Override
//    public void savePurchase(PurchaseDTO purchaseDTO) {
//        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
//        Transaction transaction = null; // Inicializa una transacción
//
//        try {
//            transaction = session.beginTransaction(); // Inicia la transacción
//            Purchase purchase = PurchaseMapper.toPurchase(purchaseDTO); // Convierte el DTO en una entidad
//            session.save(purchase); // Guarda la compra en la base de datos
//            transaction.commit(); // Confirma la transacción
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback(); // Si ocurre un error, revierte la transacción
//            }
//            e.printStackTrace();
//        } finally {
//            session.close(); // Cierra la sesión de Hibernate
//        }
//    }

    /**
     * Guarda una nueva compra en la base de datos, incluyendo sus detalles.
     *
     * @param purchaseDTO El DTO de la compra a ser guardada.
     */
    @Override
    public void savePurchase(PurchaseDTO purchaseDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Purchase purchase = PurchaseMapper.toPurchase(purchaseDTO); // Convierte el DTO en una entidad

            // Guarda la compra principal en la base de datos
            session.save(purchase);

            // Ahora, guarda los detalles de la compra en la base de datos
            List<PurchaseDetailDTO> purchaseDetails = purchaseDTO.getPurchaseDetails();
            for (PurchaseDetailDTO purchaseDetailDTO : purchaseDetails) {
                PurchaseDetail purchaseDetail = PurchaseDetailMapper.toPurchaseDetail(purchaseDetailDTO);
                purchaseDetail.setPurchase(purchase); // Establece la relación con la compra principal
                session.save(purchaseDetail);
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
     * Actualiza una compra existente en la base de datos.
     *
     * @param purchaseDTO El DTO de la compra a ser actualizada.
     */
    @Override
    public void updatePurchase(PurchaseDTO purchaseDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Purchase purchase = PurchaseMapper.toPurchase(purchaseDTO); // Convierte el DTO en una entidad
            session.update(purchase); // Actualiza la compra en la base de datos
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
     * Elimina una compra y sus detalles de la base de datos.
     *
     * @param purchaseId El ID de la compra que se va a eliminar.
     */
    @Override
    public void deletePurchase(Long purchaseId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción

            // Obtén la compra por su ID
            Purchase purchase = session.get(Purchase.class, purchaseId);

            if (purchase != null) {
                // Obtén los detalles de compra relacionados con la compra
                List<PurchaseDetail> purchaseDetails = purchase.getPurchaseDetails();

                // Itera a través de los detalles de compra
                for (PurchaseDetail purchaseDetail : purchaseDetails) {
                    // Obtén el producto relacionado con el detalle de compra
                    Product product = purchaseDetail.getProduct();

                    // Disminuye el stock del producto restando la cantidad del detalle de compra
                    int currentStock = product.getAvailableStock();
                    int quantityPurchased = purchaseDetail.getQuantityPurchased();
                    product.setAvailableStock(currentStock - quantityPurchased);

                    // Actualiza el producto en la base de datos
                    session.update(product);

                    // Elimina el detalle de compra
                    session.delete(purchaseDetail);
                }

                // Elimina la compra
                session.delete(purchase);
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
     * Obtiene los detalles de compra por el ID de compra.
     *
     * @param purchaseId El ID de la compra para la cual se desean obtener los detalles.
     * @return Una lista de DTOs de detalles de compra relacionados con la compra especificada.
     */
    @Override
    public List<PurchaseDetailDTO> getPurchaseDetailsByPurchaseId(Long purchaseId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<PurchaseDetailDTO> purchaseDetails = null; // Inicializa una lista de detalles de compra

        try {
            // Realiza una consulta para obtener los detalles de compra por el ID de compra
            Query<PurchaseDetail> query = session.createQuery("FROM PurchaseDetail WHERE purchase.purchaseId = :purchaseId", PurchaseDetail.class);
            query.setParameter("purchaseId", purchaseId);
            List<PurchaseDetail> purchaseDetailEntities = query.list();

            // Convierte las entidades en DTOs
            purchaseDetails = PurchaseDetailMapper.toPurchaseDetailDTOList(purchaseDetailEntities);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return purchaseDetails;
    }


    /**
     * Obtiene el ID de la última compra realizada en la base de datos.
     *
     * @return El ID de la última compra o 0 si no hay compras o en caso de error.
     */
    @Override
    public Long getLastPurchaseId() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate

        try {
            // Realiza una consulta para obtener el último ID de compra basado en la fecha de compra
            Query<Long> query = session.createQuery(
                    "SELECT MAX(p.purchaseId) FROM Purchase p",
                    Long.class
            );

            Long result = query.uniqueResult();

            if (result == null) {
                return 0L; // Devuelve 0 si no hay compras
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L; // Manejo de errores, devuelve 0 en caso de error
        } finally {
            session.close(); // Cierra la sesión de Hibernate en el bloque finally
        }
    }



}
