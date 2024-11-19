package com.capitole.pricingservice.application.port.in;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import com.capitole.pricingservice.application.port.in.model.PriceCreateCommand;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.application.port.in.model.PriceUpdateCommand;

import java.util.Optional;


public interface PriceService {

    /**
     * Retrieves the price for a given brand and product at a specific application time.
     *
     * @param priceQuery the query containing the brandId, productId and applicationDate
     * @return an Optional containing the Price if found, or an empty Optional if not
     */
    Optional<PriceSummary> getPriceByApplicationTimeBrandIdProductId(PriceQuery priceQuery);

    /**
     * Creates a new price and saves it into the database
     *
     * @param price the command containing the data for the new price
     * @return the saved price
     */
    Price save (PriceCreateCommand price );


    /**
     * Updates a price in the database
     *
     * @param price the command containing the data for the updated price
     * @return the updated price
     */
    Optional<Price> update(PriceUpdateCommand price);

}

