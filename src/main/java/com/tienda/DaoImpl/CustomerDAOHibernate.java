package com.tienda.DaoImpl;

import com.tienda.Dao.CustomerDAO;
import com.tienda.Model.Customer;
import com.tienda.dto.CustomerDTO;
import com.tienda.mapper.CustomerMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomerDAOHibernate implements CustomerDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public CustomerDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param customerId El ID del cliente que se desea obtener.
     * @return El DTO del cliente si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción
        CustomerDTO customerDTO = null; // Inicializa un DTO de cliente

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Customer customer = session.get(Customer.class, customerId); // Obtiene el cliente por su ID
            customerDTO = CustomerMapper.toCustomerDTO(customer); // Convierte la entidad en un DTO
            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return customerDTO; // Devuelve el DTO del cliente
    }

    /**
     * Obtiene todos los clientes.
     *
     * @return Una lista de DTOs de clientes.
     */
    @Override
    public List<CustomerDTO> getAllCustomers() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<CustomerDTO> customerDTOs = null; // Inicializa una lista de DTOs de clientes

        try {
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class); // Consulta todos los clientes
            List<Customer> customers = query.list(); // Obtiene la lista de clientes
            customerDTOs = CustomerMapper.toCustomerDTOList(customers); // Convierte las entidades en DTOs
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return customerDTOs; // Devuelve la lista de DTOs de clientes
    }

    /**
     * Guarda un nuevo cliente en la base de datos.
     *
     * @param customerDTO El DTO del cliente a ser guardado.
     */
    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Customer customer = CustomerMapper.toCustomer(customerDTO); // Convierte el DTO en una entidad
            session.save(customer); // Guarda el cliente en la base de datos
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
     * Actualiza un cliente existente en la base de datos.
     *
     * @param customerDTO El DTO del cliente a ser actualizado.
     */
    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Customer customer = CustomerMapper.toCustomer(customerDTO); // Convierte el DTO en una entidad
            session.update(customer); // Actualiza el cliente en la base de datos
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
     * Elimina un cliente por su ID desde la base de datos.
     *
     * @param customerId El ID del cliente que se desea eliminar.
     */
    @Override
    public void deleteCustomer(Long customerId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Customer customer = session.get(Customer.class, customerId); // Obtiene el cliente por su ID
            if (customer != null) {
                session.delete(customer); // Elimina el cliente de la base de datos
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
     * Verifica si un DNI ya existe en la base de datos de clientes.
     *
     * @param dni El número de DNI a verificar.
     * @return true si el DNI ya existe, false en caso contrario.
     */
    @Override
    public boolean isDniExists(String dni) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean exists = false; // Inicializa la variable de existencia

        try {
            // Crea una consulta Hibernate para contar registros en la tabla "Customer" donde el campo "customerDni" coincide con el valor proporcionado.
            // Se espera que la consulta devuelva un valor numérico (Long) que representa el recuento de coincidencias.
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Customer WHERE customerDni = :dni", Long.class);
            query.setParameter("dni", dni); // Establece el valor del parámetro "dni" en la consulta Hibernate.

            Long count = query.uniqueResult(); // Obtiene el resultado único como Long
            exists = count != null && count > 0; // Verifica si el recuento es mayor que 0
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return exists;
    }

    /**
     * Verifica si un RUC ya existe en la base de datos de clientes.
     *
     * @param ruc El número de RUC a verificar.
     * @return true si el RUC ya existe, false en caso contrario.
     */
    @Override
    public boolean isRucExists(String ruc) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean exists = false; // Inicializa la variable de existencia

        try {
            // Crea una consulta Hibernate para contar registros en la tabla "Customer" donde el campo "customerRuc" coincide con el valor proporcionado.
            // Se espera que la consulta devuelva un valor numérico (Long) que representa el recuento de coincidencias.
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Customer WHERE customerRuc = :ruc", Long.class);
            query.setParameter("ruc", ruc); // Establece el valor del parámetro "ruc" en la consulta Hibernate.

            Long count = query.uniqueResult(); // Obtiene el resultado único como Long
            exists = count != null && count > 0; // Verifica si el recuento es mayor que 0
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return exists;
    }

    /**
     * Verifica si un correo electrónico ya existe en la base de datos de clientes.
     *
     * @param email El correo electrónico a verificar.
     * @return true si el correo electrónico ya existe, false en caso contrario.
     */
    @Override
    public boolean isEmailExists(String email) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean exists = false; // Inicializa la variable de existencia

        try {
            // Crea una consulta Hibernate para contar registros en la tabla "Customer" donde el campo "customerEmail" coincide con el valor proporcionado.
            // Se espera que la consulta devuelva un valor numérico (Long) que representa el recuento de coincidencias.
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Customer WHERE customerEmail = :email", Long.class);
            query.setParameter("email", email); // Establece el valor del parámetro "email" en la consulta Hibernate.
            Long count = query.uniqueResult(); // Obtiene el resultado único como Long
            exists = count != null && count > 0; // Verifica si el recuento es mayor que 0
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return exists;
    }


    /**
     * Busca clientes en función de un criterio de búsqueda proporcionado. El criterio se compara con varios campos
     * de la entidad Customer, incluyendo nombres, apellidos, DNI, RUC, dirección, número de teléfono y correo electrónico.
     *
     * @param criteria El criterio de búsqueda a aplicar.
     * @return Una lista de CustomerDTO que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<CustomerDTO> searchCustomersBySingleCriteria(String criteria) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class); // Crea una consulta de criterios para la entidad Customer
            Root<Customer> root = criteriaQuery.from(Customer.class); // Establece la raíz de la consulta en la entidad Customer

            // Crea un predicado de búsqueda que compara el criterio con varios campos de la entidad Customer
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("customerFirstName"), "%" + criteria + "%"), // Nombres del cliente
                    criteriaBuilder.like(root.get("customerLastName"), "%" + criteria + "%"), // Apellidos del cliente
                    criteriaBuilder.equal(root.get("customerDni"), criteria), // DNI del cliente
                    criteriaBuilder.equal(root.get("customerRuc"), criteria), // RUC del cliente
                    criteriaBuilder.like(root.get("customerAddress"), "%" + criteria + "%"), // Dirección del cliente
                    criteriaBuilder.equal(root.get("customerPhoneNumber"), criteria), // Número de teléfono del cliente
                    criteriaBuilder.like(root.get("customerEmail"), "%" + criteria + "%") // Correo electrónico del cliente
            );

            criteriaQuery.where(searchPredicate); // Establece el predicado de búsqueda en la consulta
            Query<Customer> query = session.createQuery(criteriaQuery); // Crea una consulta Hibernate
            List<Customer> customers = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            return CustomerMapper.toCustomerDTOList(customers); // Convierte los resultados a DTO y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    @Override
    public List<CustomerDTO> searchCustomersByDniOrRuc(String criteria) {

        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class); // Crea una consulta de criterios para la entidad Customer
            Root<Customer> root = criteriaQuery.from(Customer.class); // Establece la raíz de la consulta en la entidad Customer

            // Crea un predicado de búsqueda que compara el criterio con varios campos de la entidad Customer
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("customerDni"), criteria), // DNI del cliente
                    criteriaBuilder.equal(root.get("customerRuc"), criteria)  // RUC del cliente

            );

            criteriaQuery.where(searchPredicate); // Establece el predicado de búsqueda en la consulta
            Query<Customer> query = session.createQuery(criteriaQuery); // Crea una consulta Hibernate
            List<Customer> customers = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            return CustomerMapper.toCustomerDTOList(customers); // Convierte los resultados a DTO y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    /**
     * Obtiene un cliente por su DNI o RUC.
     *
     * @param dniOrRuc El DNI o RUC del cliente que se desea obtener.
     * @return El DTO del cliente si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public CustomerDTO getCustomerByDniOrRuc(String dniOrRuc) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> root = criteriaQuery.from(Customer.class);

            Predicate dniPredicate = criteriaBuilder.equal(root.get("customerDni"), dniOrRuc);
            Predicate rucPredicate = criteriaBuilder.equal(root.get("customerRuc"), dniOrRuc);

            Predicate searchPredicate = criteriaBuilder.or(dniPredicate, rucPredicate);

            criteriaQuery.select(root)
                    .where(searchPredicate);

            Customer customer = session.createQuery(criteriaQuery).uniqueResult();

            return CustomerMapper.toCustomerDTO(customer);
        } finally {
            session.close();
        }
    }


}
