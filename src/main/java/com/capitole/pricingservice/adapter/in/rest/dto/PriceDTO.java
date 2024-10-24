package com.capitole.pricingservice.adapter.in.rest.dto;

import java.time.LocalDateTime;


public record PriceDTO(Long productId, Integer brandId, Integer priceList, LocalDateTime startDate,
                       LocalDateTime endDate, Double price) {
}

