package com.capitole.pricingservice.application.port.out;

import com.capitole.pricingservice.application.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface   PriceRepository {
    /**
     * Retrieves the price for a given brand and product at a specific application time.
     *
     * @param brandId the identifier of the brand
     * @param productId the identifier of the product
     * @param applicationDate the date and time for which the price is requested
     * @return an Optional containing the Price if found, or an empty Optional if not
     */
    Optional<Price> getPriceByBrandIdAndProductIdAndApplicationTime(Integer brandId, Long productId, LocalDateTime applicationDate);
}
