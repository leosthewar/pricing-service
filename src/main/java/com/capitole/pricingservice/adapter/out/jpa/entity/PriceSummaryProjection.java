package com.capitole.pricingservice.adapter.out.jpa.entity;

import java.time.LocalDateTime;

public interface PriceSummaryProjection {

    Long getProductId();
    Integer getBrandId();
    Integer getPriceList();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    Double getPrice();
    String getCurrency();
}