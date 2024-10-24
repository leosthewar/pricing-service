package com.capitole.pricingservice.application.port.in;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;

import java.util.Optional;


public interface PriceService {
    /*
     * Retrieves the price for a given brand and product at a specific application time.
     *
     * @param brandId the identifier of the brand
     * @param productId the identifier of the product
     * @param applicationDate the date and time for which the price is requested
     * @return an Optional containing the Price if found, or an empty Optional if not
     */
    Optional<Price> getPriceByApplicationTimeBrandIdProductId(PriceQuery priceQuery);
}

