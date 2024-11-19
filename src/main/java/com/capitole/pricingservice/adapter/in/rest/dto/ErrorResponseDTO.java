package com.capitole.pricingservice.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name = "error", description = "A error response")
public record ErrorResponseDTO (String code, String message, String details) {
}
