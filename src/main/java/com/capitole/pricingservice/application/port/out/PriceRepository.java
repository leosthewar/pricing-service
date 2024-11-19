package com.capitole.pricingservice.application.port.out;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;

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
    Optional<PriceSummary> getPriceByBrandIdAndProductIdAndApplicationTime(Integer brandId, Long productId, LocalDateTime applicationDate);

    /**
     * Saves a price to the database.
     *
     * @param price the price to be saved
     * @return the saved price
     */
    Price save(Price price);

    /**
     * Retrieves a price by id from the database.
     *
     * @param id the id of the price to be retrieved
     * @return an Optional containing the Price if found, or an empty Optional if not
     */
    Optional<Price> findById(Long id);


}
