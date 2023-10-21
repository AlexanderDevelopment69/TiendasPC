package com.tienda.mapper;

import com.tienda.Model.Sale;
import com.tienda.Model.SaleDetail;
import com.tienda.Model.User;
import com.tienda.dto.SaleDTO;
import com.tienda.dto.SaleDetailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO toSaleDTO(Sale sale) {
        if(sale==null){
            return null;
        }

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setSaleId(sale.getSaleId());
        saleDTO.setSaleDateTime(sale.getSaleDateTime());
        saleDTO.setSubtotal(sale.getSubtotal());
        saleDTO.setDiscountTotal(sale.getDiscountTotal());
        saleDTO.setTotal(sale.getTotal());


//
//        // Mapear la lista de SaleDetail a SaleDetailDTO utilizando Streams
//        if (sale.getSaleDetails() != null) {
//            List<SaleDetailDTO> saleDetailDTOList = sale.getSaleDetails()
//                    .stream()
//                    .map(SaleDetailMapper::toSaleDetailDTO) // Utiliza el método de mapeo de SaleDetailMapper
//                    .collect(Collectors.toList());
//            saleDTO.setSaleDetails(saleDetailDTOList);
//        }


        // Mapea el campo customer utilizando CustomerMapper
        saleDTO.setCustomer(CustomerMapper.toCustomerDTO(sale.getCustomer()));
        // Mapea el campo user utilizando UserMapper
        saleDTO.setUser(UserMapper.toUserDTO(sale.getUser()));




        // Configura otros atributos del DTO según sea necesario
        return saleDTO;
    }

    public static Sale toSale(SaleDTO saleDTO) {
        if(saleDTO==null){
            return null;
        }

        Sale sale = new Sale();
        sale.setSaleId(saleDTO.getSaleId());
        sale.setSaleDateTime(saleDTO.getSaleDateTime());
        sale.setSubtotal(saleDTO.getSubtotal());
        sale.setDiscountTotal(saleDTO.getDiscountTotal());
        sale.setTotal(saleDTO.getTotal());

//        if (saleDTO.getCustomerId() != null) {
//            Customer customer = new Customer();
//            customer.setCustomerId(saleDTO.getCustomerId());
//            sale.setCustomer(customer);
//        }
//
//        if (saleDTO.getUserId() != null) {
//            User user = new User();
//            user.setUserId(saleDTO.getUserId());
//            sale.setUser(user);
//        }


        // Mapea el campo customer utilizando CustomerMapper
        sale.setCustomer(CustomerMapper.toCustomer(saleDTO.getCustomer()));

        // Mapea el campo user utilizando UserMapper
        sale.setUser(UserMapper.toUser(saleDTO.getUser()));

        return sale;
    }


    public static List<SaleDTO> toListSaleDTO(List<Sale> sales) {
        return sales.stream()
                .map(SaleMapper::toSaleDTO)
                .collect(Collectors.toList());
    }



}
