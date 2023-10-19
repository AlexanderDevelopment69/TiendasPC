package com.tienda.mapper;

import com.tienda.Model.Product;
import com.tienda.Model.ProductCategory;
import com.tienda.Model.Supplier;
import com.tienda.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO toProductDTO(Product product) {
        // Crea un nuevo objeto ProductDTO
        ProductDTO productDTO = new ProductDTO();

        // Establece los valores de los campos del Product en el ProductDTO
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductBrand(product.getProductBrand());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setProductImage(product.getProductImage()); // Establece la imagen del producto
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setAvailableStock(product.getAvailableStock());

        // Verifica si la categoría del producto no es nula y establece su ID en el ProductDTO
        if (product.getProductCategory() != null) {
            productDTO.setProductCategoryId(product.getProductCategory().getCategoryId());
            productDTO.setProductCategoryName(product.getProductCategory().getCategoryName());
        }

        // Verifica si el proveedor del producto no es nulo y establece su ID en el ProductDTO
        if (product.getSupplier() != null) {
            productDTO.setSupplierId(product.getSupplier().getSupplierId());
            productDTO.setSupplierName(product.getSupplier().getSupplierName());
        }


        // Devuelve el ProductDTO completo con los datos mapeados
        return productDTO;
    }


    /**
     * Convierte un objeto ProductDTO en un objeto Product.
     *
     * @param productDTO El objeto ProductDTO que se va a convertir.
     * @return Un objeto Product generado a partir del ProductDTO.
     */
    public static Product toProduct(ProductDTO productDTO) {
        // Verifica si el ProductDTO es nulo
        if (productDTO == null) {
            return null; // Devuelve null si el ProductDTO es nulo
        }

        // Crea un nuevo objeto Product
        Product product = new Product();

        // Asigna el ID desde el ProductDTO al Product
        product.setProductId(productDTO.getProductId());

        // Asigna el nombre desde el ProductDTO al Product
        product.setProductName(productDTO.getProductName());

        // Asigna la marca desde el ProductDTO al Product
        product.setProductBrand(productDTO.getProductBrand());

        // Asigna la descripción desde el ProductDTO al Product
        product.setProductDescription(productDTO.getProductDescription());

        // Asigna la imagen desde el ProductDTO al Product (si está disponible)
        product.setProductImage(productDTO.getProductImage());

        // Asigna el precio desde el ProductDTO al Product
        product.setUnitPrice(productDTO.getUnitPrice());

        // Asigna el stock disponible desde el ProductDTO al Product
        product.setAvailableStock(productDTO.getAvailableStock());

        // Crea una instancia de ProductCategory y asigna el ID desde el ProductDTO
        ProductCategory category = new ProductCategory();
        category.setCategoryId(productDTO.getProductCategoryId());
        // Asigna la categoría al Product
        product.setProductCategory(category);

        // Crea una instancia de Supplier y asigna el ID desde el ProductDTO
        Supplier supplier = new Supplier();
        supplier.setSupplierId(productDTO.getSupplierId());
        // Asigna el proveedor al Product
        product.setSupplier(supplier);

        // Puedes manejar la asignación de las propiedades relacionadas aquí

        // Devuelve el objeto Product generado
        return product;
    }


    public static List<ProductDTO> toProductDTOList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());
    }
}
