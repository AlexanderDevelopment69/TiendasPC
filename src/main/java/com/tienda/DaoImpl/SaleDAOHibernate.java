package com.tienda.DaoImpl;

import com.tienda.Dao.SaleDAO;
import com.tienda.Model.Product;
import com.tienda.Model.Sale;
import com.tienda.Model.SaleDetail;
import com.tienda.dto.SaleDTO;
import com.tienda.dto.SaleDetailDTO;
import com.tienda.mapper.SaleDetailMapper;
import com.tienda.mapper.SaleMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SaleDAOHibernate implements SaleDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public SaleDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene una venta por su ID.
     *
     * @param saleId El ID de la venta que se desea obtener.
     * @return El DTO de la venta si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public SaleDTO getSaleById(Long saleId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        SaleDTO saleDTO = null;

        try {
            Sale sale = session.get(Sale.class, saleId); // Obtiene el objeto Sale por su ID
            saleDTO = SaleMapper.toSaleDTO(sale); // Convierte la entidad en un DTO
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return saleDTO;
    }

    /**
     * Obtiene todas las ventas.
     *
     * @return Una lista de DTOs de ventas.
     */
    @Override
    public List<SaleDTO> getAllSales() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<SaleDTO> saleDTOs = null; // Inicializa una lista de DTOs de ventas

        try {
            Query<Sale> query = session.createQuery("FROM Sale", Sale.class); // Consulta todas las ventas
            List<Sale> sales = query.list(); // Obtiene la lista de ventas
            saleDTOs = SaleMapper.toListSaleDTO(sales); // Convierte las entidades en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return saleDTOs; // Devuelve la lista de DTOs de ventas
    }

//    /**
//     * Guarda una nueva venta en la base de datos.
//     *
//     * @param saleDTO El DTO de la venta a ser guardada.
//     */
//    @Override
//    public void saveSale(SaleDTO saleDTO) {
//        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
//        Transaction transaction = null; // Inicializa una transacción
//
//        try {
//            transaction = session.beginTransaction(); // Inicia la transacción
//            Sale sale = SaleMapper.toSale(saleDTO); // Convierte el DTO en una entidad
//            session.save(sale); // Guarda la venta en la base de datos
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
     * Guarda una nueva venta en la base de datos, incluyendo sus detalles de venta.
     *
     * @param saleDTO El DTO de la venta a ser guardada.
     */
    @Override
    public void saveSale(SaleDTO saleDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Sale sale = SaleMapper.toSale(saleDTO); // Convierte el DTO de venta en una entidad de venta
            session.save(sale); // Guarda la venta en la base de datos

            // Ahora, guarda los detalles de venta en la base de datos
            List<SaleDetailDTO> saleDetails = saleDTO.getSaleDetails();
            for (SaleDetailDTO saleDetailDTO : saleDetails) {
                SaleDetail saleDetail = SaleDetailMapper.toSaleDetail(saleDetailDTO);
                saleDetail.setSale(sale); // Establece la relación con la venta principal
                session.save(saleDetail);
            }

            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace(); // Maneja la excepción (recomendado: registra o maneja adecuadamente)
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }



    /**
     * Actualiza una venta existente en la base de datos.
     *
     * @param saleDTO El DTO de la venta a ser actualizada.
     */
    @Override
    public void updateSale(SaleDTO saleDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Sale sale = SaleMapper.toSale(saleDTO); // Convierte el DTO en una entidad
            session.update(sale); // Actualiza la venta en la base de datos
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
     * Elimina una venta y sus detalles de la base de datos. También actualiza el stock de productos
     * relacionados con los detalles de venta.
     *
     * @param saleId El ID de la venta que se va a eliminar.
     */
    @Override
    public void deleteSale(Long saleId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción

            // Obtén la venta por su ID
            Sale sale = session.get(Sale.class, saleId);

            if (sale != null) {
                // Obtén los detalles de venta relacionados con la venta
                List<SaleDetail> saleDetails = sale.getSaleDetails();

                // Itera a través de los detalles de venta
                for (SaleDetail saleDetail : saleDetails) {
                    // Obtén el producto relacionado con el detalle de venta
                    Product product = saleDetail.getProduct();

                    // Aumenta el stock del producto sumando la cantidad del detalle de venta
                    int currentStock = product.getAvailableStock();
                    int quantitySold = saleDetail.getQuantitySold();
                    product.setAvailableStock(currentStock + quantitySold);

                    // Actualiza el producto en la base de datos
                    session.update(product);

                    // Elimina el detalle de venta
                    session.delete(saleDetail);
                }

                // Elimina la venta
                session.delete(sale);
            }

            transaction.commit();  // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }






    /**
     * Obtiene el ID de la última venta realizada en la base de datos.
     *
     * @return El ID de la última venta o 0L si no se encuentra ninguna venta o si ocurre un error.
     */
    @Override
    public Long getLastSaleId() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Long lastSaleId = 0L; // Valor predeterminado

        try {
            // Realiza una consulta para obtener el último ID de venta basado en la fecha de venta
            Query<Long> query = session.createQuery(
                    "SELECT MAX(s.saleId) FROM Sale s",
                    Long.class
            );

            lastSaleId = query.uniqueResult(); // Obtiene el resultado único como Long
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores, imprime la excepción
        } finally {
            session.close(); // Cierra la sesión de Hibernate en el bloque "finally"
        }

        return lastSaleId != null ? lastSaleId : 0L; // Devuelve "0L" si lastSaleId es null
    }

    /**
     * Obtiene los detalles de venta por el ID de venta.
     *
     * @param saleId El ID de la venta para la cual se desean obtener los detalles.
     * @return Una lista de DTOs de detalles de venta relacionados con la venta especificada.
     */
    @Override
    public List<SaleDetailDTO> getSaleDetailsBySaleId(Long saleId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<SaleDetailDTO> saleDetails = null; // Inicializa una lista de detalles de venta

        try {
            // Realiza una consulta para obtener los detalles de venta por el ID de venta
            Query<SaleDetail> query = session.createQuery("FROM SaleDetail WHERE sale.saleId = :saleId", SaleDetail.class);
            query.setParameter("saleId", saleId);
            List<SaleDetail> saleDetailEntities = query.list();

            // Convierte las entidades en DTOs
            saleDetails = SaleDetailMapper.toSaleDetailDTOList(saleDetailEntities);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return saleDetails;
    }


}

