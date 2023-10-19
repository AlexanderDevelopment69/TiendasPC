package com.tienda.DaoImpl;

import com.tienda.Dao.ProductDAO;
import com.tienda.Model.Product;
import com.tienda.Model.ProductCategory;
import com.tienda.Model.Supplier;
import com.tienda.dto.ProductDTO;
import com.tienda.mapper.ProductMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.Collections;
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


    /**
     * Busca productos en función de un criterio de búsqueda proporcionado. El criterio se compara con varios campos
     * de la entidad Product, incluyendo nombre, marca, nombre del proveedor, nombre de la categoría de producto,
     * descripción, precio y stock.
     *
     * @param criteria El criterio de búsqueda a aplicar.
     * @return Una lista de ProductDTO que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<ProductDTO> searchProductsBySingleCriteria(String criteria) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class); // Crea una consulta de criterios para la entidad Product
            Root<Product> root = criteriaQuery.from(Product.class); // Establece la raíz de la consulta en la entidad Product

            // Crea una predicado de búsqueda que compara el criterio con varios campos de la entidad Product
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("productName"), "%" + criteria + "%"), // Nombre del producto
                    criteriaBuilder.like(root.get("productBrand"), "%" + criteria + "%"), // Marca del producto
                    criteriaBuilder.like(root.join("supplier").get("supplierName"), "%" + criteria + "%"), // Nombre del proveedor
                    criteriaBuilder.like(root.join("productCategory").get("categoryName"), "%" + criteria + "%"), // Nombre de la categoría del producto
                    criteriaBuilder.like(root.get("productDescription"), "%" + criteria + "%"), // Descripción del producto
                    criteriaBuilder.like(root.get("unitPrice").as(String.class), "%" + criteria + "%"), // Precio del producto
                    criteriaBuilder.like(root.get("availableStock").as(String.class), "%" + criteria + "%") // Stock disponible del producto
            );

            criteriaQuery.where(searchPredicate); // Establece el predicado de búsqueda en la consulta
            Query<Product> query = session.createQuery(criteriaQuery); // Crea una consulta Hibernate
            List<Product> products = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            return ProductMapper.toProductDTOList(products); // Convierte los resultados a DTO y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }




    /**
     * Obtiene una lista de productos que pertenecen a un proveedor específico.
     *
     * @param supplierId El ID del proveedor cuyos productos se desean obtener.
     * @return Una lista de DTOs de productos que pertenecen al proveedor especificado.
     */
    @Override
    public List<ProductDTO> getProductsBySupplier(Long supplierId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class); // Crea una consulta de criterios para la entidad Product
            Root<Product> root = criteriaQuery.from(Product.class); // Establece la raíz de la consulta en la entidad Product

            // Crea un predicado que compara el ID del proveedor con el proveedor asociado en la entidad Product
            Predicate supplierPredicate = criteriaBuilder.equal(root.get("supplier").get("supplierId"), supplierId);

            criteriaQuery.select(root)
                    .where(supplierPredicate); // Establece el predicado en la consulta

            List<Product> products = session.createQuery(criteriaQuery).list(); // Ejecuta la consulta y obtiene los resultados

            return ProductMapper.toProductDTOList(products); // Convierte los resultados a DTOs y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }



    /**
     * Actualiza el stock de un producto en la base de datos.
     *
     * @param productId     El ID del producto cuyo stock se actualizará.
     * @param quantityToUpdate La cantidad en la que se actualizará el stock. Puede ser un valor positivo o negativo
     *                        para aumentar o disminuir el stock respectivamente.
     */
    @Override
    public void updateProductStock(Long productId, int quantityToUpdate) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Product product = session.get(Product.class, productId); // Obtiene el producto por su ID

            if (product != null) {
                // Calcula el nuevo stock
                int currentStock = product.getAvailableStock();
                int newStock = currentStock + quantityToUpdate;
                product.setAvailableStock(newStock); // Establece el nuevo stock en el objeto Product

                session.update(product); // Actualiza el producto en la base de datos
                transaction.commit(); // Confirma la transacción
            } else {
                // El producto no se encontró en la base de datos, puedes manejar esto apropiadamente.
                // Puedes lanzar una excepción, registrar un mensaje de error, etc.
            }
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
     * Actualiza el stock de un producto al realizar una venta.
     *
     * @param productId      El ID del producto que se vendió.
     * @param quantitySold   La cantidad de productos vendidos.
     */
    public void updateProductStockOnSale(Long productId, int quantitySold) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            Product product = session.get(Product.class, productId); // Obtiene el producto por su ID

            if (product != null) {
                int currentStock = product.getAvailableStock();

                // Verifica que haya suficiente stock antes de realizar la venta
                if (currentStock >= quantitySold) {
                    int newStock = currentStock - quantitySold;
                    product.setAvailableStock(newStock); // Actualiza el stock restante

                    session.update(product); // Actualiza el producto en la base de datos
                    transaction.commit(); // Confirma la transacción
                } else {
                    // Manejar el caso de stock insuficiente (puede lanzar una excepción, registrar un mensaje de error, etc.)
                }
            } else {
                // Manejar el caso de producto no encontrado en la base de datos (puede lanzar una excepción, registrar un mensaje de error, etc.)
            }
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
     * Busca un producto por nombre, categoría o ID.
     *
     * @param searchCriteria El criterio de búsqueda que puede ser el nombre, la categoría o el ID del producto.
     * @return El DTO del producto si se encuentra, de lo contrario, null.
     */
    @Override
    public ProductDTO getProductByProductNameOrCategoryOrId(String searchCriteria) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);

//            Predicate namePredicate = criteriaBuilder.like(root.get("productName"), "%" + searchCriteria + "%");
            Predicate categoryPredicate = criteriaBuilder.equal(root.get("productCategory").get("categoryName"), searchCriteria);

//            Predicate idPredicate = criteriaBuilder.equal(root.get("productId"), Long.parseLong(searchCriteria));

            Predicate searchPredicate = criteriaBuilder.or( categoryPredicate);

            criteriaQuery.select(root)
                    .where(searchPredicate);

            Product product = session.createQuery(criteriaQuery).uniqueResult();

            return ProductMapper.toProductDTO(product);
        } catch (NumberFormatException e) {
            // Manejo de excepción si el criterio no es un número válido (en caso de búsqueda por ID)
            return null; // Devuelve null si no se encontraron resultados válidos
        } finally {
            session.close();
        }
    }




    /**
     * Busca productos en función de un criterio de búsqueda proporcionado. El criterio puede ser el nombre, la categoría o el ID del producto.
     *
     * @param searchCriteria El criterio de búsqueda a aplicar (nombre, categoría o ID del producto).
     * @return Una lista de DTOs de productos que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<ProductDTO> searchProductByProductNameOrCategoryOrId(String searchCriteria) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);

//            Predicate namePredicate = criteriaBuilder.like(root.get("productName"), "%" + searchCriteria + "%");
//            Predicate categoryPredicate = criteriaBuilder.equal(root.get("productCategory").get("categoryName"), searchCriteria);
            Predicate categoryPredicate = criteriaBuilder.like(root.get("productCategory").get("categoryName"), "%" + searchCriteria + "%");
//            Predicate idPredicate = criteriaBuilder.equal(root.get("productId"), Long.parseLong(searchCriteria));

            Predicate searchPredicate = criteriaBuilder.or(categoryPredicate);

            criteriaQuery.select(root)
                    .where(searchPredicate);

            List<Product> products = session.createQuery(criteriaQuery).list();

            return ProductMapper.toProductDTOList(products);
        } catch (NumberFormatException e) {
            // Manejo de excepción si el criterio no es un número válido (en caso de búsqueda por ID)
            return Collections.emptyList(); // Devuelve una lista vacía si no se encontraron resultados válidos
        } finally {
            session.close();
        }
    }


}
