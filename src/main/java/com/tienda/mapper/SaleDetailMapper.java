package com.tienda.mapper;

import com.tienda.Model.Product;
import com.tienda.Model.Sale;
import com.tienda.Model.SaleDetail;
import com.tienda.dto.SaleDetailDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SaleDetailMapper {
    public static SaleDetailDTO toSaleDetailDTO(SaleDetail saleDetail) {
        SaleDetailDTO saleDetailDTO = new SaleDetailDTO();
        saleDetailDTO.setDetailId(saleDetail.getDetailId());
        saleDetailDTO.setQuantitySold(saleDetail.getQuantitySold());
        saleDetailDTO.setDiscountPerProduct(saleDetail.getDiscountPerProduct());
        saleDetailDTO.setSubtotalPerProduct(saleDetail.getSubtotalPerProduct());
//
//        if (saleDetail.getSale() != null) {
//            saleDetailDTO.setSaleId(saleDetail.getSale().getSaleId());
//        }
//
//        if (saleDetail.getProduct() != null) {
//            saleDetailDTO.setProductId(saleDetail.getProduct().getProductId());
//            saleDetailDTO.setProductName(saleDetail.getProduct().getProductName());
//            saleDetailDTO.setProductBrand(saleDetail.getProduct().getProductBrand());
//            saleDetailDTO.setProductDescription(saleDetail.getProduct().getProductDescription());
//            saleDetailDTO.setUnitPrice(saleDetail.getProduct().getUnitPrice());
//        }

        // Mapea la relación Sale utilizando SaleMapper
        saleDetailDTO.setSale(SaleMapper.toSaleDTO(saleDetail.getSale()));

        // Mapea la relación Product utilizando ProductMapper
        saleDetailDTO.setProduct(ProductMapper.toProductDTO(saleDetail.getProduct()));

        return saleDetailDTO;
    }

    public static SaleDetail toSaleDetail(SaleDetailDTO saleDetailDTO) {
        if (saleDetailDTO == null) {
            return null;
        }

        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setDetailId(saleDetailDTO.getDetailId());
        saleDetail.setQuantitySold(saleDetailDTO.getQuantitySold());
        saleDetail.setDiscountPerProduct(saleDetailDTO.getDiscountPerProduct());
        saleDetail.setSubtotalPerProduct(saleDetailDTO.getSubtotalPerProduct());

//        // Establecer la relación con Sale
//        Sale sale = new Sale();
//        sale.setSaleId(saleDetailDTO.getSaleId());
//        saleDetail.setSale(sale);
//
//        // Establecer la relación con Product
//        Product product = new Product();
//        product.setProductId(saleDetailDTO.getProductId());
//        saleDetail.setProduct(product);

        // Mapea la relación Sale utilizando SaleMapper
        saleDetail.setSale(SaleMapper.toSale(saleDetailDTO.getSale()));

        // Mapea la relación Product utilizando ProductMapper
        saleDetail.setProduct(ProductMapper.toProduct(saleDetailDTO.getProduct()));

        return saleDetail;
    }



    public static List<SaleDetailDTO> toSaleDetailDTOList(List<SaleDetail> saleDetails) {
        if (saleDetails == null) {
            return Collections.emptyList();
        }

        return saleDetails.stream()
                .map(SaleDetailMapper::toSaleDetailDTO)
                .collect(Collectors.toList());
    }


}
