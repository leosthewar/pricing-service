package com.capitole.pricingservice.application.domain.model;


import java.time.LocalDateTime;

public record PriceSummary(Integer brandId, Long productId,
                           LocalDateTime startDate, LocalDateTime endDate, Integer priceList,
                           Double price, CurrencyEnum currency) {
}
