package com.capitole.pricingservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.capitole.pricingservice.common.validation.BeanValidation.validate;

/*
 * Class model for handle  the inputs for price query and validate them
 */
public record PriceQuery(@NotNull LocalDateTime applicationTime,
                         @NotNull Integer brandId,
                         @NotNull Long productId) {

    public PriceQuery( LocalDateTime applicationTime,
                       Integer brandId,
                       Long productId) {
        this.applicationTime = applicationTime;
        this.brandId = brandId;
        this.productId = productId;
        validate(this);
    }
}
