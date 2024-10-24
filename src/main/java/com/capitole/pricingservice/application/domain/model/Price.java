package com.capitole.pricingservice.application.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class Price {

    private Integer brandId;
    private Long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer priceList;
    private Integer priority;
    private Double price;
    private String currency;

}
