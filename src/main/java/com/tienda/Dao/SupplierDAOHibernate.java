package com.tienda.Dao;

import com.tienda.Model.Supplier;
import com.tienda.dto.SupplierDTO;
import com.tienda.mapper.SupplierMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SupplierDAOHibernate implements SupplierDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public SupplierDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene un proveedor por su ID.
     *
     * @param supplierId El ID del proveedor que se desea obtener.
     * @return El DTO del proveedor si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public SupplierDTO getSupplierById(Long supplierId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        SupplierDTO supplierDTO = null; // Inicializa un DTO de proveedor

        try {
            // Obtiene el proveedor por su ID
            Supplier supplier = session.get(Supplier.class, supplierId);
            // Convierte la entidad en un DTO
            supplierDTO = SupplierMapper.toSupplierDTO(supplier);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return supplierDTO; // Devuelve el DTO del proveedor
    }

    /**
     * Obtiene todos los proveedores.
     *
     * @return Una lista de DTOs de proveedores.
     */
    @Override
    public List<SupplierDTO> getAllSuppliers() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<SupplierDTO> supplierDTOs = null; // Inicializa una lista de DTOs de proveedores

        try {
            // Consulta todos los proveedores en la base de datos
            Query<Supplier> query = session.createQuery("FROM Supplier", Supplier.class);
            // Obtiene la lista de proveedores
            List<Supplier> suppliers = query.list();
            // Convierte las entidades en DTOs
            supplierDTOs = SupplierMapper.toSupplierDTOList(suppliers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return supplierDTOs; // Devuelve la lista de DTOs de proveedores
    }

    /**
     * Guarda un nuevo proveedor en la base de datos.
     *
     * @param supplierDTO El DTO del proveedor a ser guardado.
     */
    @Override
    public void saveSupplier(SupplierDTO supplierDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Convierte el DTO en una entidad
            Supplier supplier = SupplierMapper.toSupplier(supplierDTO);
            // Guarda el proveedor en la base de datos
            session.save(supplier);
            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // Si ocurre un error, revierte la transacción
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    /**
     * Actualiza un proveedor existente en la base de datos.
     *
     * @param supplierDTO El DTO del proveedor a ser actualizado.
     */
    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Convierte el DTO en una entidad
            Supplier supplier = SupplierMapper.toSupplier(supplierDTO);
            // Actualiza el proveedor en la base de datos
            session.update(supplier);
            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // Si ocurre un error, revierte la transacción
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    /**
     * Elimina un proveedor por su ID desde la base de datos.
     *
     * @param supplierId El ID del proveedor que se desea eliminar.
     */
    @Override
    public void deleteSupplier(Long supplierId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            // Obtiene el proveedor por su ID
            Supplier supplier = session.get(Supplier.class, supplierId);
            if (supplier != null) {
                // Elimina el proveedor de la base de datos
                session.delete(supplier);
            }
            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // Si ocurre un error, revierte la transacción
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    /**
     * Verifica si ya existe un proveedor con un RUC específico en la base de datos.
     *
     * @param ruc El RUC del proveedor a verificar.
     * @return true si existe un proveedor con el mismo RUC, false en caso contrario.
     */
    @Override
    public boolean existsSupplierWithRuc(String ruc) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        try {
            // Crea una consulta que cuenta la cantidad de proveedores con el mismo RUC
            Query query = session.createQuery("SELECT COUNT(*) FROM Supplier s WHERE s.supplierRuc = :ruc");
            // Establece el parámetro RUC en la consulta
            query.setParameter("ruc", ruc);
            // Ejecuta la consulta y obtiene el resultado como un Long
            Long count = (Long) query.getSingleResult();
            // Retorna true si hay al menos un proveedor con el mismo RUC, false si no hay ninguno
            return count > 0;
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }

    /**
     * Verifica si ya existe un proveedor con un correo electrónico específico en la base de datos.
     *
     * @param email El correo electrónico del proveedor a verificar.
     * @return true si existe un proveedor con el mismo correo electrónico, false en caso contrario.
     */
    @Override
    public boolean existsSupplierWithEmail(String email) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        try {
            // Crea una consulta que cuenta la cantidad de proveedores con el mismo correo electrónico
            Query query = session.createQuery("SELECT COUNT(*) FROM Supplier s WHERE s.supplierEmail = :email");
            // Establece el parámetro de correo electrónico en la consulta
            query.setParameter("email", email);
            // Ejecuta la consulta y obtiene el resultado como un Long
            Long count = (Long) query.getSingleResult();
            // Retorna true si hay al menos un proveedor con el mismo correo electrónico, false si no hay ninguno
            return count > 0;
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }
}
