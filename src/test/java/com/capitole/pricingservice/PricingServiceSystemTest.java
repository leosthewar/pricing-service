package com.capitole.pricingservice;

import com.capitole.pricingservice.adapter.in.rest.dto.PriceDTO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

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
@Sql(scripts = "classpath:/system-test-data.sql")
@TestPropertySource(properties = "spring.sql.init.mode=never")
class PricingServiceSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @CsvSource({
            "1, 35455, 2020-06-14 10:00:00, OK, 35.5",
            "1, 35455, 2020-06-14 16:00:00, OK, 25.45",
            "1, 35455, 2020-06-14 21:00:00, OK, 35.5",
            "1, 35455, 2020-06-15 10:00:00, OK, 30.5",
            "1, 35455, 2020-06-15 21:00:00, OK , 38.95"
    })
    @ParameterizedTest
    void priceByBrandIdAndProductId_returnsOK(Integer brandId, Long productId, String applicationDate, HttpStatus expectedStatus, Double expectedPrice) {

        //Given - Build the URL with query parameters
        String baseUrl = "/api/prices/{brandId}/{productId}";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("applicationDate", applicationDate)
                .buildAndExpand(brandId, productId)
                .toUriString();

        //When - Make the request
        ResponseEntity<PriceDTO> response = restTemplate.getForEntity(uri, PriceDTO.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(expectedStatus);

        if (response.getStatusCode().is2xxSuccessful()) {
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
        String baseUrl = "/api/prices/{brandId}/{productId}";
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

}

