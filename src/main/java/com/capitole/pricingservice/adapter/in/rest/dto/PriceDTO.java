package com.capitole.pricingservice.adapter.in.rest.dto;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name = "priceSummary", description = "A price summary", example = "{\"productId\": 35455,\"brandId\": 1,\"priceList\": 1,\"startDate\": \"2020-06-14 00:00:00\",\"endDate\": \"2020-12-31 23:59:59\",\"price\": 35.5}")
public record PriceDTO(Long productId, Integer brandId, Integer priceList,
                       @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                       LocalDateTime startDate,
                       @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                       LocalDateTime endDate, Double price, String currency) {

    public static PriceDTO toPriceDTO(PriceSummary price) {
        return new PriceDTO(price.productId(),
                price.brandId(), price.priceList(),
                price.startDate(), price.endDate(),
                price.price(), price.currency().toString());
    }

    public static PriceDTO toPriceDTO(Price price) {
        return new PriceDTO(price.getProductId(),
                price.getBrandId(), price.getPriceList(),
                price.getStartDate(), price.getEndDate(),
                price.getPrice(), price.getCurrency().toString());
    }
}

