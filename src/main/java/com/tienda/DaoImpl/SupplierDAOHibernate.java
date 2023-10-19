package com.tienda.DaoImpl;

import com.tienda.Dao.SupplierDAO;
import com.tienda.Model.Supplier;
import com.tienda.dto.SupplierDTO;
import com.tienda.mapper.SupplierMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    /**
     * Busca proveedores en función de un criterio de búsqueda proporcionado. El criterio se compara con varios campos
     * de la entidad Supplier, incluyendo nombre, RUC, dirección, número de teléfono y correo electrónico.
     *
     * @param criteria El criterio de búsqueda a aplicar.
     * @return Una lista de SupplierDTO que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<SupplierDTO> searchSuppliersBySingleCriteria(String criteria) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class); // Crea una consulta de criterios para la entidad Supplier
            Root<Supplier> root = criteriaQuery.from(Supplier.class); // Establece la raíz de la consulta en la entidad Supplier

            // Crea un predicado de búsqueda que compara el criterio con varios campos de la entidad Supplier
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("supplierName"), "%" + criteria + "%"), // Nombre del proveedor
                    criteriaBuilder.like(root.get("supplierRuc"), "%" + criteria + "%"), // RUC del proveedor
                    criteriaBuilder.like(root.get("supplierAddress"), "%" + criteria + "%"), // Dirección del proveedor
                    criteriaBuilder.like(root.get("supplierPhoneNumber"), "%" + criteria + "%"), // Número de teléfono del proveedor
                    criteriaBuilder.like(root.get("supplierEmail"), "%" + criteria + "%") // Correo electrónico del proveedor
            );

            criteriaQuery.where(searchPredicate); // Establece el predicado de búsqueda en la consulta
            Query<Supplier> query = session.createQuery(criteriaQuery); // Crea una consulta Hibernate
            List<Supplier> suppliers = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            return SupplierMapper.toSupplierDTOList(suppliers); // Convierte los resultados a DTO y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    @Override
    public SupplierDTO getSupplierBySupplierNameOrRuc(String searchCriteria) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
            Root<Supplier> root = criteriaQuery.from(Supplier.class);

            Predicate namePredicate = criteriaBuilder.equal(root.get("supplierName"), searchCriteria);
            Predicate rucPredicate = criteriaBuilder.equal(root.get("supplierRuc"), searchCriteria);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.or(namePredicate, rucPredicate));

            Supplier supplier = session.createQuery(criteriaQuery).uniqueResult();

            return SupplierMapper.toSupplierDTO(supplier);
        } finally {
            session.close();
        }
    }

}
