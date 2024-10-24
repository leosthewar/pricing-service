package com.capitole.pricingservice.adapter.in.rest.dto;

import com.capitole.pricingservice.application.domain.model.Price;
public class PriceDTOMapper {

    public static PriceDTO toPriceDTO(Price price) {
        return new PriceDTO(price.getProductId(),
                price.getBrandId(), price.getPriceList(),
                price.getStartDate(), price.getEndDate(),
                price.getPrice());
    }

}
