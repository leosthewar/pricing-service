package com.capitole.pricingservice.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Price {

    private final Long id;
    private final Integer brandId;
    private final Long productId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priceList;
    private final Double price;
    private final CurrencyEnum currency;
    private final Integer priority;


    public Price(Long id, Integer brandId, Long productId, LocalDateTime startDate,
                 LocalDateTime endDate, Integer priceList, Double price,
                 CurrencyEnum currency, Integer priority) {

        if (id == null || brandId == null || productId == null || currency == null) {
            throw new IllegalArgumentException("id, brandId, productId and currency must not be null.");
        }
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before endDate and not null.");
        }
        if (priceList == null || priceList < 0) {
            throw new IllegalArgumentException("price list must not be null or negative.");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("price must not be null or negative.");
        }
        if (priority == null || priority < 0) {
            throw new IllegalArgumentException("priority must not be null or negative.");
        }


        this.id = id;
        this.brandId = brandId;
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.price = price;
        this.currency = currency;
        this.priority = priority;
    }

    /**
     * Constructor to create a price with default id and priority,
     * to use just to create a price save in persistence
     */
    public Price(Integer brandId, Long productId, LocalDateTime startDate,
                 LocalDateTime endDate, Integer priceList, Double price, CurrencyEnum currency) {

        if (brandId == null || productId == null || currency == null) {
            throw new IllegalArgumentException("brandId, productId and currency must not be null.");
        }
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before endDate and not null.");
        }
        if (priceList == null || priceList < 0) {
            throw new IllegalArgumentException("price list must not be null or negative.");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("price must not be null or negative.");
        }

        this.id = null;
        this.brandId = brandId;
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.price = price;
        this.currency = currency;
        this.priority = 0;
    }
}

