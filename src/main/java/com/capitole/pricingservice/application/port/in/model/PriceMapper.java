package com.capitole.pricingservice.application.port.in.model;

import com.capitole.pricingservice.application.domain.model.Price;

public class PriceMapper {
    public static Price toPriceToCreateWithDefaultPriority(PriceCreateCommand priceCommand) {
        return new Price( priceCommand.brandId(), priceCommand.productId(),
                priceCommand.startDate(), priceCommand.endDate(), priceCommand.priceList(),
                priceCommand.price(), priceCommand.currency());
    }
}
