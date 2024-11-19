package com.capitole.pricingservice.application.port.in.model;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

import static com.capitole.pricingservice.common.validation.BeanValidation.validate;

public record PriceUpdateCommand(
        @NotNull Long id,
        @NotNull Integer brandId,
        @NotNull Long productId,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        @NotNull @PositiveOrZero Integer priceList,
        @NotNull @PositiveOrZero Double price,
        @NotNull CurrencyEnum currency) {

    public PriceUpdateCommand(Long id,Integer brandId, Long productId, LocalDateTime startDate,
                              LocalDateTime endDate, Integer priceList, Double price, CurrencyEnum currency) {

        this.id = id;
        this.brandId = brandId;
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.price = price;
        this.currency = currency;
        validate(this);
    }
}