package com.capitole.pricingservice.adapter.out.jpa.entity;

import com.capitole.pricingservice.application.domain.model.Price;

public class PriceEntityMapper {

    public static Price toPrice(PriceSummaryProjection priceSummary) {
        return new Price(priceSummary.getBrandId(), priceSummary.getProductId(), priceSummary.getStartDate(),
                priceSummary.getEndDate(),
                priceSummary.getPriceList(), priceSummary.getPrice());
    }
}
