package com.tienda.Dao;

import com.tienda.Model.Product;
import com.tienda.Model.ProductCategory;
import com.tienda.Model.Supplier;
import com.tienda.dto.ProductDTO;
import com.tienda.mapper.ProductMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor para ProductDAOHibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public ProductDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param productDTO El DTO del producto a ser guardado.
     */
    @Override
    public void saveProduct(ProductDTO productDTO) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();

            // Verifica si la categoría y el proveedor existen en la base de datos
            ProductCategory productCategory = session.get(ProductCategory.class, productDTO.getProductCategoryId());
            Supplier supplier = session.get(Supplier.class, productDTO.getSupplierId());

            // Verifica si la categoría y el proveedor existen antes de continuar
            if (productCategory != null && supplier != null) {
                // Convierte el DTO en una entidad Product
                Product product = ProductMapper.toProduct(productDTO);

                // Establece las relaciones con la categoría y el proveedor
                product.setProductCategory(productCategory);
                product.setSupplier(supplier);

                // Guarda el nuevo producto en la base de datos
                session.save(product);

                // Confirma la transacción
                transaction.commit();
            } else {
                // Manejar el caso si la categoría o el proveedor no existen
                // Puedes lanzar una excepción o manejarlo de acuerdo a tus necesidades
                // Por ejemplo:
                throw new Exception("La categoría o el proveedor especificados no existen en la base de datos.");
            }
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }


    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param productDTO El DTO del producto a ser actualizado.
     */
    @Override
    public void updateProduct(ProductDTO productDTO) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();
            // Convierte el DTO en una entidad Product
            Product product = ProductMapper.toProduct(productDTO);

            // Actualiza el producto existente en la base de datos
            session.update(product);

            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }

    /**
     * Obtiene un producto por su ID desde la base de datos.
     *
     * @param productId El ID del producto que se desea obtener.
     * @return El DTO del producto si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public ProductDTO getProductById(Long productId) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa un DTO de producto
        ProductDTO productDTO = null;

        try {
            // Obtiene un producto por su ID desde la base de datos
            Product product = session.get(Product.class, productId);
            if (product != null) {
                // Convierte la entidad Product en un DTO
                productDTO = ProductMapper.toProductDTO(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }

        return productDTO;
    }

    /**
     * Obtiene todos los productos desde la base de datos.
     *
     * @return Una lista de DTOs de productos.
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una lista de DTOs de productos
        List<ProductDTO> productDTOs = null;

        try {
            // Obtiene todos los productos desde la base de datos
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            List<Product> products = query.list();

            // Convierte la lista de entidades Product en una lista de DTOs
            productDTOs = ProductMapper.toProductDTOList(products);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }

        return productDTOs;
    }

    /**
     * Elimina un producto por su ID desde la base de datos.
     *
     * @param productId El ID del producto que se desea eliminar.
     */
    @Override
    public void deleteProduct(Long productId) {
        // Abre una nueva sesión de Hibernate
        Session session = sessionFactory.openSession();
        // Inicializa una transacción
        Transaction transaction = null;

        try {
            // Inicia la transacción
            transaction = session.beginTransaction();

            // Obtiene un producto por su ID desde la base de datos y lo elimina
            Product product = session.get(Product.class, productId);
            if (product != null) {
                session.delete(product);
            }

            // Confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate
            session.close();
        }
    }
}
