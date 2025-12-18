package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.SellerDTO;
import com.hackerrank.sample.model.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    public SellerDTO toDTO(Seller seller) {
        return SellerDTO.builder().id(seller.getId()).name(seller.getName()).reputation(seller.getReputation())
                .totalSales(seller.getTotalSales()).yearsActive(seller.getYearsActive())
                .responseTime(seller.getResponseTime()).build();
    }
}
