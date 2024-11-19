package com.capitole.pricingservice.adapter.in.rest.dto;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import com.capitole.pricingservice.application.port.in.model.PriceUpdateCommand;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Schema(name = "priceToUpdate", description = "A price to update",example= "{\"brandId\":1,\"productId\":35455,\"priceList\":1,\"startDate\":\"2020-06-14 00:00:00\",\"endDate\":\"2020-12-31 23:59:59\",\"price\":35.5,\"currency\":\"EUR\"}")
public record PriceToUpdateDTO(
        @Schema(description = "brand id", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer brandId,
        @Schema(description = "product id", requiredMode = Schema.RequiredMode.REQUIRED)
        Long productId,
        @Schema(description = "price list", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer priceList,
        @Schema(description = "start date of the price - yyyy-MM-dd HH:mm:ss", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime startDate,
        @Schema(description = "end date of the price - yyyy-MM-dd HH:mm:ss", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime endDate,
        @Schema(description = "price", requiredMode = Schema.RequiredMode.REQUIRED)
        Double price,
        @Schema(description = "currency code", requiredMode = Schema.RequiredMode.REQUIRED)
        String currency
) {


    public PriceUpdateCommand toPriceUpdateCommand(Long id) {
        if (this.startDate == null || this.endDate == null)
            throw new DateTimeParseException("Start date and End date must be not null", "", 0);

        if (this.startDate.isAfter(this.endDate))
            throw new IllegalArgumentException("Start date must be before end date.");

        return new PriceUpdateCommand(id,this.brandId, this.productId,
                this.startDate, this.endDate, this.priceList,
                this.price, CurrencyEnum.fromCode(this.currency));
    }
}

