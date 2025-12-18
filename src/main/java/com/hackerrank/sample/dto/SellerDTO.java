package com.hackerrank.sample.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {
    private Long id;
    private String name;
    private Integer reputation;
    private Integer totalSales;
    private Integer yearsActive;
    private String responseTime;
}
