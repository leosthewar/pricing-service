package com.capitole.pricingservice.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO (String code, String message, String details) {
}
