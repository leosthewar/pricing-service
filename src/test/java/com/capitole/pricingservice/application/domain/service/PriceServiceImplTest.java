package com.capitole.pricingservice.application.domain.service;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.application.port.out.PriceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
/**
 * Unit tests for the PriceServiceImpl.
 * <p>
 * This class includes test cases to verify the behavior of the PriceServiceImpl methods,
 * ensuring that the correct results are returned under various scenarios. It uses Mockito
 * to mock the dependencies and verify interactions with the PriceRepository.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
public class PriceServiceImplTest {

    private final PriceRepository priceRepository =  Mockito.mock(PriceRepository.class);

    private final PriceServiceImpl priceService = new PriceServiceImpl(priceRepository);

    @Test
    void getPriceByApplicationTimeBrandIdProductId_ReturnsPrice_WhenPriceExists() {
        // Given - Build the PriceQuery and mock the PriceRepository response
        LocalDateTime applicationDateTime = LocalDateTime.parse("2022-01-01 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PriceQuery priceQuery = new PriceQuery(applicationDateTime, 1122, 1234L);
        Price price = new Price(1122, 1234L, LocalDateTime.now(), LocalDateTime.now(), 1, 20.0);
        when(priceRepository.getPriceByBrandIdAndProductIdAndApplicationTime(any(), any(), any())).thenReturn(Optional.of(price));

        // When - Call the getPriceByApplicationTimeBrandIdProductId method
        Optional<Price> result = priceService.getPriceByApplicationTimeBrandIdProductId(priceQuery);

        // Then - Verify that the result is correct
        // Assert that the result is present since the price exists
        assertTrue(result.isPresent());
        assertEquals(price, result.get());

        // Verify that the repository method is called with the correct arguments
        then(priceRepository).should()
                .getPriceByBrandIdAndProductIdAndApplicationTime(eq(1122), eq(1234L), eq(applicationDateTime));

    }
    @Test
    void getPriceByApplicationTimeBrandIdProductId_ReturnsEmpty_WhenPriceDoesNotExist() {
        // Given - Build the PriceQuery and mock the PriceRepository response
        LocalDateTime applicationDateTime = LocalDateTime.parse("2022-01-01 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PriceQuery priceQuery = new PriceQuery(applicationDateTime, 1122, 1234L);
        when(priceRepository.getPriceByBrandIdAndProductIdAndApplicationTime(any(), any(), any())).thenReturn(Optional.empty());

        // When - Call the getPriceByApplicationTimeBrandIdProductId method
        Optional<Price> result = priceService.getPriceByApplicationTimeBrandIdProductId(priceQuery);

        // Then - Verify that the result is correct
        // Assert that the result is empty since the price does not exist
        assertTrue(result.isEmpty());

        // Verify that the repository method is called with the correct arguments
        then(priceRepository).should()
                .getPriceByBrandIdAndProductIdAndApplicationTime(eq(1122), eq(1234L), eq(applicationDateTime));
    }

}
