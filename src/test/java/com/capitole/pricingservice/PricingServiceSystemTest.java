package com.capitole.pricingservice;

import com.capitole.pricingservice.adapter.in.rest.dto.PriceDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

/*
 * This class contains system tests for the Pricing Service application.
 *
 * The tests use the TestRestTemplate to send requests to the application and
 * verify the responses. The tests also use the @Sql annotation to load test data
 * from scripts in the classpath.
 *
 * In a real environment with the final DBMS, we might consider  using a more robust approach,
 * such as using a test data builder or a library like Testcontainers.
 *
 * The tests verify the responses in two different ways:
 *
 * 1. The HTTP status code is verified to ensure that the request was
 * successfully processed.
 * 2. The response body is verified to ensure that the response contains the
 * expected data according to use case
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Sql(scripts = "classpath:/system-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // to reset the DB after each test
class PricingServiceSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/api/prices";

    @CsvSource({
            "1, 35455, 2020-06-14 10:00:00, OK, 35.5",
            "1, 35455, 2020-06-14 16:00:00, OK, 25.45",
            "1, 35455, 2020-06-14 21:00:00, OK, 35.5",
            "1, 35455, 2020-06-15 10:00:00, OK, 30.5",
            "1, 35455, 2020-06-15 21:00:00, OK , 38.95"
    })
    @Sql(scripts = "classpath:/system-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @ParameterizedTest

    void priceByBrandIdAndProductId_returnsOK(Integer brandId, Long productId, String applicationDate, HttpStatus expectedStatus, Double expectedPrice) {

        //Given - Build the URL with query parameters
        String baseUrl = ENDPOINT+"/{brandId}/{productId}";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("applicationDate", applicationDate)
                                         .buildAndExpand(brandId, productId)
                                         .toUriString();

        //When - Make the request
        ResponseEntity<PriceDTO> response = restTemplate.getForEntity(uri, PriceDTO.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

        if (response.getStatusCode()
                    .is2xxSuccessful()) {
            // check the response body and price
            PriceDTO body = response.getBody();
            then(body).isNotNull();
            then(body.price()).isEqualTo(expectedPrice);
        }
    }


    @CsvSource({
            "1, 35455, 2020-06-13 14:00:00, NOT_FOUND",
            "1, 35455, 2021-06-13 14:00:00, NOT_FOUND",
            "1, 35455, 2020-06-13, BAD_REQUEST",
            "1, 35455, , BAD_REQUEST",
            "1, , 2020-06-13 14:00:00, NOT_FOUND",
            " , , 2020-06-13 14:00:00, NOT_FOUND"
    })
    @ParameterizedTest
    void priceByBrandIdAndProductId_returnsError(Integer brandId, Long productId, String applicationDate, HttpStatus expectedStatus) {
        //Given -  Build the URL with query parameters
        String baseUrl = ENDPOINT+"/{brandId}/{productId}";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("applicationDate", applicationDate)
                                         .buildAndExpand(brandId, productId)
                                         .toUriString();

        // when - Make the request
        ResponseEntity<PriceDTO> response = restTemplate.getForEntity(uri, PriceDTO.class);

        // Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

    }

    @CsvSource({
            "1, 35455, 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, CREATED ",
            "1, 35455, 1, 2020-06-14 16:00:00, 2020-06-15 16:00:00, 25.45, EUR, CREATED",
            "1, 35455, 1, 2020-06-14 21:00:00, 2020-06-15 21:00:00, 35.5, EUR, CREATED",
            "1, 35455, 1, 2020-06-15 10:00:00, 2020-06-16 10:00:00, 30.5, EUR, CREATED",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15 21:00:00, 38.95, EUR, CREATED "
    })
    @ParameterizedTest
    void savePrice_OK(Integer brandId, Long productId, Integer priceList, String startDate, String endDate,
                      Double price, String currency, HttpStatus expectedStatus) {

        //Given - Build the URL with query parameters
        String uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                                         .build()
                                         .toUriString();
        LocalDateTime startDateLocal = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateLocal = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PriceDTO priceDTO = new PriceDTO(productId, brandId, priceList, startDateLocal, endDateLocal, price, currency);

        //When - Make the request

        ResponseEntity<PriceDTO> response = restTemplate.postForEntity(uri, priceDTO, PriceDTO.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

        if (response.getStatusCode()
                    .isSameCodeAs(HttpStatus.CREATED)) {
            // check the response body and price
            PriceDTO body = response.getBody();
            then(body).isNotNull();
            then(body).isEqualTo(priceDTO);
        }
    }


    @CsvSource({
            ", 35455 , 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, BAD_REQUEST",
            "1, , 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, BAD_REQUEST",
            "1, 35455, , 2020-06-14 16:00:00, 2020-06-15 16:00:00, 25.45, EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-14 21:00:00, 2020-06-15 21:00:00, , EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 10:00:00, 2020-06-16 10:00:00, 30.5, , BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15 21:00:00, 30.5, CurrencyError, BAD_REQUEST",
            "1, 35455, 1, , 2020-06-15 21:00:00, 30.5, EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 21:00:00, , 30.5, CurrencyError, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15, 2020-06-15 21:00:00, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, abc, 2020-06-15 21:00:00, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, abc, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-14 21:00:00, 38.95, EUR, BAD_REQUEST "
    })
    @ParameterizedTest
    void savePrice_Error(Integer brandId, Long productId, Integer priceList, String startDate, String endDate,
                         Double price, String currency, HttpStatus expectedStatus) throws JsonProcessingException {

        //Given - Build the URL with query parameters
        String uri = UriComponentsBuilder.fromUriString(ENDPOINT)
                                         .build()
                                         .toUriString();


        Map<String, Object> requestBody = new HashMap<>();
        if(productId != null) requestBody.put("productId", productId);
        if(brandId != null) requestBody.put("brandId", brandId);
        if(priceList != null) requestBody.put("priceList", priceList);
        if(startDate != null) requestBody.put("startDate", startDate);
        if(endDate != null) requestBody.put("endDate", endDate);
        if(price != null) requestBody.put("price", price);
        if(currency != null) requestBody.put("currency", currency);
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        //When - Make the request

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<PriceDTO> response = restTemplate.postForEntity(uri, entity, PriceDTO.class);

        //Then - Check the response
        // Assert the status
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

    }

    @CsvSource({
            "1, 35455, 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, OK ",
            "1, 35455, 1, 2020-06-14 16:00:00, 2020-06-15 16:00:00, 25.45, EUR, OK",
            "1, 35455, 1, 2020-06-14 21:00:00, 2020-06-15 21:00:00, 35.5, EUR, OK",
            "1, 35455, 1, 2020-06-15 10:00:00, 2020-06-16 10:00:00, 30.5, EUR, OK",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15 21:00:00, 38.95, EUR, OK "
    })
    @ParameterizedTest
    void updatePrice_OK(Integer brandId, Long productId, Integer priceList, String startDate, String endDate,
                      Double price, String currency, HttpStatus expectedStatus) {

        //Given - Build the URL with query parameters
        String baseUrl = ENDPOINT+"/{id}";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .buildAndExpand(1L)
                                         .toUriString();
        LocalDateTime startDateLocal = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateLocal = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PriceDTO priceDTO = new PriceDTO(productId, brandId, priceList, startDateLocal, endDateLocal, price, currency);

        //When - Make the request

        ResponseEntity<PriceDTO> response = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(priceDTO),
                PriceDTO.class
        );

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

        if (response.getStatusCode()
                    .isSameCodeAs(HttpStatus.CREATED)) {
            // check the response body and price
            PriceDTO body = response.getBody();
            then(body).isNotNull();
            then(body).isEqualTo(priceDTO);
        }
    }


    @CsvSource({
            ", 35455 , 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, BAD_REQUEST",
            "1, , 1, 2020-06-14 10:00:00, 2020-06-15 10:00:00, 35.5, EUR, BAD_REQUEST",
            "1, 35455, , 2020-06-14 16:00:00, 2020-06-15 16:00:00, 25.45, EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-14 21:00:00, 2020-06-15 21:00:00, , EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 10:00:00, 2020-06-16 10:00:00, 30.5, , BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15 21:00:00, 30.5, CurrencyError, BAD_REQUEST",
            "1, 35455, 1, , 2020-06-15 21:00:00, 30.5, EUR, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15 21:00:00, , 30.5, CurrencyError, BAD_REQUEST",
            "1, 35455, 1, 2020-06-15, 2020-06-15 21:00:00, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-15, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, abc, 2020-06-15 21:00:00, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, abc, 38.95, EUR, BAD_REQUEST ",
            "1, 35455, 1, 2020-06-15 21:00:00, 2020-06-14 21:00:00, 38.95, EUR, BAD_REQUEST "
    })
    @ParameterizedTest
    void updatePrice_Error(Integer brandId, Long productId, Integer priceList, String startDate, String endDate,
                         Double price, String currency, HttpStatus expectedStatus) throws JsonProcessingException {

        //Given - Build the URL with query parameters
        String baseUrl = ENDPOINT+"/{id}";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .buildAndExpand(1L)
                                         .toUriString();


        Map<String, Object> requestBody = new HashMap<>();
        if(productId != null) requestBody.put("productId", productId);
        if(brandId != null) requestBody.put("brandId", brandId);
        if(priceList != null) requestBody.put("priceList", priceList);
        if(startDate != null) requestBody.put("startDate", startDate);
        if(endDate != null) requestBody.put("endDate", endDate);
        if(price != null) requestBody.put("price", price);
        if(currency != null) requestBody.put("currency", currency);
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        //When - Make the request

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<PriceDTO> response = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                entity,
                PriceDTO.class
        );



        //Then - Check the response
        // Assert the status
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

    }


}

