package com.capitole.pricingservice.adapter.in.rest.controller;

import com.capitole.pricingservice.adapter.in.rest.dto.PriceToCreateDTO;
import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import com.capitole.pricingservice.application.port.in.PriceService;
import com.capitole.pricingservice.application.port.in.model.PriceCreateCommand;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.application.port.in.model.PriceUpdateCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ObjectMapper objectMapper;


    private static final String ENDPOINT = "/api/prices";


    @Test
    public void testGetPrice() throws Exception {

        // Given - Build the URL with query parameters and a mock response
        String applicationDate = "2024-05-01 12:00:00";
        LocalDateTime applicationDateTime = LocalDateTime.parse(applicationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{brandId}/{productId}")
                                      .queryParam("applicationDate", applicationDate)
                                      .buildAndExpand(41, 42L)
                                      .toUri();
        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willReturn(Optional.of(new PriceSummary(41,
                        42L,
                        LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2024, 12, 1, 0, 0, 0),
                        1,
                        20.0, CurrencyEnum.USD)));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isOk())
               .andExpect(jsonPath("$.brandId").value(41L))
               .andExpect(jsonPath("$.productId").value(42L))
               .andExpect(jsonPath("$.startDate").value("2024-01-01 00:00:00"))
               .andExpect(jsonPath("$.endDate").value("2024-12-01 00:00:00"))
               .andExpect(jsonPath("$.price").value(20.0));

        // Verify that the method was called with the correct arguments
        then(priceService).should()
                          .getPriceByApplicationTimeBrandIdProductId(eq(new PriceQuery(applicationDateTime, 41, 42L)));
    }

    @Test
    void testGetPriceInvalidateApplicationDate() throws Exception {

        // Given - Build the URL with query parameters
        String applicationDate = "Invalid date";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{brandId}/{productId}")
                                      .queryParam("applicationDate", applicationDate)
                                      .buildAndExpand(41, 42L)
                                      .toUri();

        // When
        ResultActions results = mockMvc.perform(get(uri));

        // Then
        // Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid  date format. Please use yyyy-MM-dd HH:mm:ss."))
               .andExpect(jsonPath("$.code").value("INVALID_DATE_FORMAT"));

        // Verify that the method was not called the service
        then(priceService).shouldHaveNoInteractions();

    }

    @Test
    void testGetPriceInvalidPriceQuery() throws Exception {
        // Given - Build the URL with query parameters and a mock ConstraintViolationException
        String applicationDate = "2024-05-01 12:00:00";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{brandId}/{productId}")
                                      .queryParam("applicationDate", applicationDate)
                                      .buildAndExpand(41, 42L)
                                      .toUri();
        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willThrow(new ConstraintViolationException("Error", null));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }

    @Test
    void testGetPriceMissingApplicationDate() throws Exception {
        // Given - Build the URL with query parameters
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{brandId}/{productId}")
                                      .buildAndExpand(41, 42L)
                                      .toUri();

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }


    @Test
    void testGetPriceUnexpectedException() throws Exception {
        // Given - Build the URL with query parameters and a mock RuntimeException
        String applicationDate = "2024-05-01 12:00:00";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{brandId}/{productId}")
                                      .queryParam("applicationDate", applicationDate)
                                      .buildAndExpand(41, 42L)
                                      .toUri();

        given(priceService.getPriceByApplicationTimeBrandIdProductId(any(PriceQuery.class)))
                .willThrow(new RuntimeException("Error", null));

        // When - Make the request
        ResultActions results = mockMvc.perform(get(uri));

        // Then - Validate the response
        results.andExpect(status().isInternalServerError());
        results.andExpect(jsonPath("$.message").value("An unexpected error occurred."))
               .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"));

    }

    @Test
    public void testSavePrice() throws Exception {

        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(41, 42L, 1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        PriceCreateCommand priceCommand = new PriceCreateCommand(41, 42L, dateTime, dateTime.plusDays(2), 1, 20.0, CurrencyEnum.EUR);
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        Price saveExpected = new Price(1L, 41, 42L, dateTime, dateTime.plusDays(2), 1, 20.0, CurrencyEnum.EUR, 0);
        given(priceService.save(any(PriceCreateCommand.class)))
                .willReturn(saveExpected);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isCreated())
               .andExpect(jsonPath("$.brandId").value(41))
               .andExpect(jsonPath("$.productId").value(42L))
               .andExpect(jsonPath("$.startDate").value("2022-01-01 12:00:00"))
               .andExpect(jsonPath("$.endDate").value("2022-01-03 12:00:00"))
               .andExpect(jsonPath("$.price").value(20.0));

        // Verify that the method was called with the correct arguments
        then(priceService).should()
                          .save(eq(priceCommand));

    }


    @Test
    public void testSavePrice_bradIdNull() throws Exception {

        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(null, 42L, 1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("brandId: must not be null"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }

    @Test
    public void testSavePrice_productIdNull() throws Exception {

        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, null, 1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("productId: must not be null"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }

    @Test
    public void testSavePrice_startDateNull() throws Exception {

        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 42L, 1, null, dateTime.plusDays(2), 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid  date format. Please use yyyy-MM-dd HH:mm:ss."))
               .andExpect(jsonPath("$.details").value("Start date and End date must be not null"))
               .andExpect(jsonPath("$.code").value("INVALID_DATE_FORMAT"));

    }

    @Test
    public void testSavePrice_endDateNull() throws Exception {

        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 42L, 1, dateTime, null, 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid  date format. Please use yyyy-MM-dd HH:mm:ss."))
               .andExpect(jsonPath("$.details").value("Start date and End date must be not null"))
               .andExpect(jsonPath("$.code").value("INVALID_DATE_FORMAT"));

    }
    @Test
    public void testSavePrice_startDateInvalid() throws Exception {

        // Given - Build  Body  and a mock response
        String jsonBody = """
                {
                  "brandId": 42,
                  "productId": 42,
                  "priceList": 1,
                  "startDate":"2022-01-03 ",
                  "endDate": "2022-01-03 00:00:00",
                  "price": 20.0,
                  "currency": "EUR"
                }""";

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }

    @Test
    public void testSavePrice_endDateInvalid() throws Exception {

        // Given - Build  Body  and a mock response
        String jsonBody = """
                {
                  "brandId": 42,
                  "productId": 42,
                  "priceList": 1,
                  "endDate":"2022-01-03 ",
                  "startDate": "2022-01-03 00:00:00",
                  "price": 20.0,
                  "currency": "EUR"
                }""";

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));

    }



    @Test
    public void testSavePrice_startDateAfterEndDate() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 12, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 1, 10, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, 1, startDate, endDate, 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("Start date must be before end date."))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void testSavePrice_currencyNull() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, 1, dateTime, dateTime.plusDays(2), 20.0, null);
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("Currency code must not be null or empty"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }


    @Test
    public void testSavePrice_priceNull() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, 1, dateTime, dateTime.plusDays(2), null, "USD");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("price: must not be null"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void testSavePrice_priceListNull() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, null, dateTime, dateTime.plusDays(2), 20.0, "USD");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("priceList: must not be null"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void testSavePrice_priceListNegative() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, -1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("priceList: must be greater than or equal to 0"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void testSavePrice_priceNegative() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, 1, dateTime, dateTime.plusDays(2), -20.0, "EUR");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("price: must be greater than or equal to 0"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }


    @Test
    public void testSavePrice_currencyInvalid() throws Exception {
        // Given - Build  Body  and a mock response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 12, 0, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(42, 1L, 1, dateTime, dateTime.plusDays(2), 20.0, "InvalidCurrency");
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        // When - Make the request
        ResultActions results = mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isBadRequest());
        results.andExpect(jsonPath("$.message").value("Invalid request. Please check the input parameters."))
               .andExpect(jsonPath("$.details").value("No matching currency for code: InvalidCurrency"))
               .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void testUpdatePrice() throws Exception {

        // Given - Build  Body  and a mock response
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{id}")
                                      .buildAndExpand(1L)
                                      .toUri();

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(41, 42L, 1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        PriceUpdateCommand priceCommand = new PriceUpdateCommand(1L, 41, 42L, dateTime, dateTime.plusDays(2), 1, 20.0, CurrencyEnum.EUR);
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        Price updateExpected = new Price(1L, 41, 42L, dateTime, dateTime.plusDays(2), 1, 20.0, CurrencyEnum.EUR, 0);
        given(priceService.update(any(PriceUpdateCommand.class)))
                .willReturn(Optional.of(updateExpected));

        // When - Make the request
        ResultActions results = mockMvc.perform(
                put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isOk())
               .andExpect(jsonPath("$.brandId").value(41))
               .andExpect(jsonPath("$.productId").value(42L))
               .andExpect(jsonPath("$.startDate").value("2022-01-01 12:00:00"))
               .andExpect(jsonPath("$.endDate").value("2022-01-03 12:00:00"))
               .andExpect(jsonPath("$.price").value(20.0));

        // Verify that the method was called with the correct arguments
        then(priceService).should()
                          .update( (eq(priceCommand)));

    }

    @Test
    public void testUpdatePrice_priceNotFound() throws Exception {

        // Given - Build  Body  and a mock response
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/{id}")
                                      .buildAndExpand(1L)
                                      .toUri();

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceToCreateDTO priceToCreateDTO = new PriceToCreateDTO(41, 42L, 1, dateTime, dateTime.plusDays(2), 20.0, "EUR");
        PriceUpdateCommand priceCommand = new PriceUpdateCommand(1L,41, 42L, dateTime, dateTime.plusDays(2), 1, 20.0, CurrencyEnum.EUR);
        // Serialize DTO into JSON
        String requestBody = objectMapper.writeValueAsString(priceToCreateDTO);

        given(priceService.update(any(PriceUpdateCommand.class)))
                .willReturn(Optional.empty());

        // When - Make the request
        ResultActions results = mockMvc.perform(
                put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then - Validate the response
        results.andExpect(status().isNotFound());

        // Verify that the method was called with the correct arguments
        then(priceService).should()
                          .update (eq(priceCommand));

    }



}