package com.capitole.pricingservice.adapter.out.jpa.entity;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;

public class PriceEntityMapper {

    public static PriceSummary toPriceFromSummary(PriceSummaryProjection priceSummary) {
        return new PriceSummary(priceSummary.getBrandId(), priceSummary.getProductId(),
                priceSummary.getStartDate(), priceSummary.getEndDate(), priceSummary.getPriceList(),
                priceSummary.getPrice(), CurrencyEnum.fromCode(priceSummary.getCurrency()));
    }

    public static Price toPrice(PriceEntity priceEntity) {
        return new Price(priceEntity.getId(), priceEntity.getBrandId(), priceEntity.getProductId(),
                priceEntity.getStartDate(), priceEntity.getEndDate(), priceEntity.getPriceList(),
                priceEntity.getPrice(), CurrencyEnum.fromCode(priceEntity.getCurrency()),
                priceEntity.getPriority());
    }


    public static PriceEntity toPriceEntity(Price price) {
        return new PriceEntity(price.getId(), price.getBrandId(), price.getProductId(),
                price.getPriceList(), price.getPriority(), price.getStartDate(),
                price.getEndDate(), price.getPrice(), price.getCurrency()
                                                           .toString());
    }
}
