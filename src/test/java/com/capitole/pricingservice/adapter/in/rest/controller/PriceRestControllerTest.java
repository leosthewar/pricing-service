package com.capitole.pricingservice.adapter.in.rest.controller;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.port.in.PriceService;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Integration tests for the PriceRestController.
 * <p>
 * This class includes test cases to verify the behavior of the PriceRestController
 * endpoints, ensuring that the correct HTTP status codes and response messages are returned
 * under various scenarios. It uses MockMvc to perform requests and Mockito to mock the
 * PriceService interactions.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
@WebMvcTest(controllers = PriceRestController.class)
public class PriceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    private static final String ENDPOINT = "/api/prices/{brandId}/{productId}";


    @Test
    public void testGetPrice() throws Exception {

        // Given - Build the URL with query parameters and a mock response
        String applicationDate = "2024-05-01 12:00:00";
        LocalDateTime applicationDateTime = LocalDateTime.parse(applicationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                .queryParam("applicationDate", applicationDate)
                .buildAndExpand(41, 42L).toUri();
        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willReturn(Optional.of(new Price(41,
                        42L,
                        LocalDateTime.of(2024,1,1,0, 0,0),
                        LocalDateTime.of(2024,12,1,0, 0,0),
                        1,
                        20.0)));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(41L))
                .andExpect(jsonPath("$.productId").value(42L))
                .andExpect(jsonPath("$.startDate").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2024-12-01T00:00:00"))
                .andExpect(jsonPath("$.price").value(20.0));

        // Verify that the method was called with the correct arguments
        then(priceService).should()
                .getPriceByApplicationTimeBrandIdProductId(eq(new PriceQuery(applicationDateTime, 41, 42L)));
    }

    @Test
    void testInvalidateApplicationDate() throws Exception {

        // Given - Build the URL with query parameters
        String applicationDate = "Invalid date";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                .queryParam("applicationDate", applicationDate)
                .buildAndExpand(41, 42L).toUri();

        // When
        ResultActions results = mockMvc.perform(get(uri));

        // Then
        // Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid application date format. Please use yyyy-MM-dd HH:mm:ss."))
                .andExpect(jsonPath("$.code").value("INVALID_DATE_FORMAT"));

        // Verify that the method was not called the service
        then(priceService).shouldHaveNoInteractions();

    }

    @Test
    void testInvalidPriceQuery() throws Exception {
        // Given - Build the URL with query parameters and a mock ConstraintViolationException
        String applicationDate = "2024-05-01 12:00:00";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                .queryParam("applicationDate", applicationDate)
                .buildAndExpand(41, 42L).toUri();
        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willThrow(new ConstraintViolationException("Error",null));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid price query. Please check the input parameters."))
                .andExpect(jsonPath("$.code").value("INVALID_PRICE_QUERY"));

    }

    @Test
    void testMissingApplicationDate() throws Exception {
        // Given - Build the URL with query parameters
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                .buildAndExpand(41, 42L).toUri();

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid price query. Please check the input parameters."))
                .andExpect(jsonPath("$.code").value("INVALID_PRICE_QUERY"));

    }

    @Test
    void testMissingResourceNotFound() throws Exception {
        // When - Make the request
        ResultActions results = mockMvc.perform(get("/api/prices/41"));

        // Then - Validate the response
        results.andExpect(status().isNotFound());
        results.andExpect(jsonPath("$.message").value("Resource not found."))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));

    }

    @Test
    void testUnexpectedException() throws Exception {
        // Given - Build the URL with query parameters and a mock RuntimeException
        String applicationDate = "2024-05-01 12:00:00";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                .queryParam("applicationDate", applicationDate)
                .buildAndExpand(41, 42L).toUri();

        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willThrow(new RuntimeException("Error",null));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isInternalServerError());
        results.andExpect(jsonPath("$.message").value("An unexpected error occurred."))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"));

    }

}