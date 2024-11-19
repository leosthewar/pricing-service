package com.capitole.pricingservice.adapter.in.rest.controller;

import com.capitole.pricingservice.adapter.in.rest.dto.ErrorResponseDTO;
import com.capitole.pricingservice.adapter.in.rest.dto.PriceDTO;
import com.capitole.pricingservice.adapter.in.rest.dto.PriceToCreateDTO;
import com.capitole.pricingservice.adapter.in.rest.dto.PriceToUpdateDTO;
import com.capitole.pricingservice.adapter.in.rest.exception.PriceNotFoundException;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.port.in.PriceService;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.common.annotation.RestAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * REST controller for handling price-related requests.
 * <p>
 * This controller provides endpoints to retrieve pricing information based on brand, product, and application date.
 * It uses {@link PriceService} to perform business logic and return the appropriate price data.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>GET /api/prices - Retrieves price information for a specific brand, product, and application date.</li>
 *   <li>POST /api/prices - Creates a new price.</li>
 *   <li>PUT /api/prices/{id} - Updates an existing price.</li>
 * </ul>
 *
 * <p>Responses:
 * <ul>
 *   <li>200 - Successful operation</li>
 *   <li>201 - Resource created</li>
 *   <li>404 - Resource not found</li>
 *   <li>400 - Bad request</li>
 *   <li>500 - Unexpected error</li>
 * </ul>
 *
 * <p>Exceptions:
 * <ul>
 *   <li>{@link PriceNotFoundException} - Thrown when no price is found for the given criteria.</li>
 * </ul>
 *
 * <p>This class is annotated with {@link RestAdapter} to indicate it's a web REST adapter component.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/prices")
@RestAdapter
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
})
class PriceRestController {

    private static final Logger logger = LoggerFactory.getLogger(PriceRestController.class);

    private final PriceService priceService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resource/Price not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @Operation(summary = "Get price by brandId and productId and applicationDate")
    @GetMapping("/{brandId}/{productId}")
    public ResponseEntity<PriceDTO> getPrice(@PathVariable(value ="brandId") Integer brandId,
                                             @PathVariable(value ="productId") Long productId,
                                             @Schema(description = "yyyy-MM-dd HH:mm:ss")
                                             @RequestParam(value = "applicationDate") String applicationDate) {
        LocalDateTime applicationDateTime = LocalDateTime.parse(applicationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Optional<PriceDTO> dto = priceService.getPriceByApplicationTimeBrandIdProductId(new PriceQuery(applicationDateTime, brandId, productId))
                                             .map(PriceDTO::toPriceDTO);
        logger.info("Requesting price for brandId: {}, productId: {}, applicationDate: {},  priceFound: {}", brandId, productId, applicationDate, dto.isPresent());
        return dto.map(ResponseEntity::ok)
                  .orElseThrow(() -> new PriceNotFoundException("Price not found for parameters in request"));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Price created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))),
    })
    @Operation(summary = "Create price")
    public ResponseEntity<PriceDTO> save(@RequestBody PriceToCreateDTO priceToCreate) {
        Price price = priceService.save(priceToCreate.toPriceCreateCommand());
        logger.info("Save price for brandId: {}, productId: {} ", priceToCreate.brandId(), priceToCreate.productId());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(PriceDTO.toPriceDTO(price));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))),
    })
    public ResponseEntity<PriceDTO> update(@RequestBody PriceToUpdateDTO priceToUpdateDTO,
                                           @PathVariable(value = "id") Long id) {

        Optional<PriceDTO> priceDTO = priceService.update(priceToUpdateDTO.toPriceUpdateCommand(id))
                                                  .map(PriceDTO::toPriceDTO);
        return priceDTO.map(ResponseEntity::ok)
                       .orElseThrow(() -> new PriceNotFoundException("Price not found"));
    }



}
