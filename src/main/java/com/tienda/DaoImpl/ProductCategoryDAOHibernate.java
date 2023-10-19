package com.tienda.DaoImpl;

import com.tienda.Dao.ProductCategoryDAO;
import com.tienda.Model.ProductCategory;
import com.tienda.dto.ProductCategoryDTO;
import com.tienda.mapper.ProductCategoryMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductCategoryDAOHibernate implements ProductCategoryDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public ProductCategoryDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene una categoría de producto por su ID.
     *
     * @param categoryId El ID de la categoría que se desea obtener.
     * @return El DTO de la categoría si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public ProductCategoryDTO getCategoryById(Long categoryId) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa un DTO de categoría
        ProductCategoryDTO categoryDTO = null;

        try {
            // Obtiene la categoría por su ID
            ProductCategory productCategory = session.get(ProductCategory.class, categoryId);
            // Convierte la entidad en un DTO
            categoryDTO = ProductCategoryMapper.toProductCategoryDTO(productCategory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }

        // Devuelve el DTO de la categoría
        return categoryDTO;
    }

    /**
     * Obtiene todas las categorías de productos.
     *
     * @return Una lista de DTOs de categorías de productos.
     */
    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una lista de DTOs de categorías
        List<ProductCategoryDTO> categoryDTOs = null;

        try {
            // Consulta todas las categorías en la base de datos
            Query<ProductCategory> query = session.createQuery("FROM ProductCategory", ProductCategory.class);
            // Obtiene la lista de categorías
            List<ProductCategory> categories = query.list();
            // Convierte las entidades en DTOs
            categoryDTOs = ProductCategoryMapper.toProductCategoryDTOList(categories);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }

        // Devuelve la lista de DTOs de categorías
        return categoryDTOs;
    }

    /**
     * Guarda una nueva categoría de producto en la base de datos.
     *
     * @param categoryDTO El DTO de la categoría a ser guardada.
     */
    @Override
    public void saveCategory(ProductCategoryDTO categoryDTO) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();
            // Convierte el DTO en una entidad
            ProductCategory productCategory = ProductCategoryMapper.toProductCategory(categoryDTO);
            // Guarda la categoría en la base de datos
            session.save(productCategory);
            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // Si ocurre un error, revierte la transacción
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }

    /**
     * Actualiza una categoría de producto existente en la base de datos.
     *
     * @param categoryDTO El DTO de la categoría a ser actualizada.
     */
    @Override
    public void updateCategory(ProductCategoryDTO categoryDTO) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();
            // Convierte el DTO en una entidad
            ProductCategory productCategory = ProductCategoryMapper.toProductCategory(categoryDTO);
            // Actualiza la categoría en la base de datos
            session.update(productCategory);
            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // Si ocurre un error, revierte la transacción
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }

    /**
     * Elimina una categoría de producto por su ID desde la base de datos.
     *
     * @param categoryId El ID de la categoría que se desea eliminar.
     */
    @Override
    public void deleteCategory(Long categoryId) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();
            // Obtiene la categoría por su ID
            ProductCategory productCategory = session.get(ProductCategory.class, categoryId);
            if (productCategory != null) {
                // Elimina la categoría de la base de datos
                session.delete(productCategory);
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
            // Cierra la sesión de Hibernate
            session.close();
        }
    }
}
