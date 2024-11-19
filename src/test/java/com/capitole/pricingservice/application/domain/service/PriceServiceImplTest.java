package com.capitole.pricingservice.application.domain.service;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import com.capitole.pricingservice.application.port.in.model.PriceCreateCommand;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.application.port.in.model.PriceUpdateCommand;
import com.capitole.pricingservice.application.port.out.PriceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private final PriceRepository priceRepository = Mockito.mock(PriceRepository.class);

    private final PriceServiceImpl priceService = new PriceServiceImpl(priceRepository);

    @Test
    void getPriceByApplicationTimeBrandIdProductId_ReturnsPrice_WhenPriceExists() {
        // Given - Build the PriceQuery and mock the PriceRepository response
        LocalDateTime applicationDateTime = LocalDateTime.parse("2022-01-01 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PriceQuery priceQuery = new PriceQuery(applicationDateTime, 1122, 1234L);
        PriceSummary price =  new PriceSummary(1122,1234L,LocalDateTime.now(),
                LocalDateTime.now(), 1,20.0, CurrencyEnum.USD);


        when(priceRepository.getPriceByBrandIdAndProductIdAndApplicationTime(any(), any(), any())).thenReturn(Optional.of(price));

        // When - Call the getPriceByApplicationTimeBrandIdProductId method
        Optional<PriceSummary> result = priceService.getPriceByApplicationTimeBrandIdProductId(priceQuery);

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
        Optional<PriceSummary> result = priceService.getPriceByApplicationTimeBrandIdProductId(priceQuery);

        // Then - Verify that the result is correct
        // Assert that the result is empty since the price does not exist
        assertTrue(result.isEmpty());

        // Verify that the repository method is called with the correct arguments
        then(priceRepository).should()
                             .getPriceByBrandIdAndProductIdAndApplicationTime(eq(1122), eq(1234L), eq(applicationDateTime));
    }

    @Test
    void savePrice_ok() {
        // Given - Build the PriceCommand and mock the PriceRepository response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceCreateCommand priceCommand = new PriceCreateCommand(1122, 1234L, dateTime,dateTime.plusDays(2), 1, 20.0, CurrencyEnum.USD);
        Price priceExpectedToCreate = new Price(1122, 1234L, dateTime,dateTime.plusDays(2), 1, 20.0, CurrencyEnum.USD);
        Price expectedResult = new Price(1L,1122, 1234L, dateTime,LocalDateTime.now().plusDays(2), 1, 20.0, CurrencyEnum.USD,0);

        when(priceRepository.save(any())).thenReturn(expectedResult);
        // When - Call the save method
        Price result = priceService.save(priceCommand);

        // Then - Verify that the result is correct
        assertEquals(expectedResult, result);

        // Verify that the repository method is called with the correct arguments
        // Capture and verify arguments
        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        then(priceRepository).should()
                             .save(captor.capture());
        Price capturedPrice = captor.getValue();
        assertEquals(priceExpectedToCreate, capturedPrice);

    }

    @Test
    void savePrice_errorInvalidDates() {
        // Given - Build the PriceCommand and mock the PriceRepository response
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        PriceCreateCommand priceCommand = new PriceCreateCommand(1122, 1234L, dateTime,dateTime.minusDays(2), 1, 20.0, CurrencyEnum.USD);

        // When and then - Call the save method
        assertThrowsExactly(IllegalArgumentException.class, () -> priceService.save(priceCommand));


    }

    @Test
    void updatePrice_ok() {
        // Given - Build the PriceCommand and mock the PriceRepository responses
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Long id = 1L;
        PriceUpdateCommand priceCommand = new PriceUpdateCommand(id,1122, 1234L, dateTime,dateTime.plusDays(2), 1, 20.0, CurrencyEnum.USD);
        Price priceExpectedToUpdate = new Price(id, 1122, 1234L, dateTime,dateTime.plusDays(2), 1, 20.0, CurrencyEnum.USD,1);
        Price priceStorage = new Price(id, 1122, 1234L, dateTime,dateTime.plusDays(2), 1, 10.0, CurrencyEnum.EUR,1);
        Price expectedResult = new Price(id,1122, 1234L, dateTime,LocalDateTime.now().plusDays(2), 1, 20.0, CurrencyEnum.USD,1);

        when(priceRepository.save(any())).thenReturn(expectedResult);
        when(priceRepository.findById(any())).thenReturn(Optional.of(priceStorage));
        // When - Call the save method
        Optional<Price> result = priceService.update(priceCommand);

        // Then - Verify that the result is correct
        assertEquals(Optional.of(expectedResult), result);

        // Verify that the repository method is called with the correct arguments
        // Capture and verify arguments
        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        then(priceRepository).should()
                             .save(captor.capture());
        Price capturedPrice = captor.getValue();
        assertEquals(priceExpectedToUpdate, capturedPrice);

    }
    @Test
    void updatePrice_errorPriceNtFound() {
        // Given - Build the PriceCommand and mock the PriceRepository responses
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Long id = 1L;
        PriceUpdateCommand priceCommand = new PriceUpdateCommand(id,1122, 1234L, dateTime,dateTime.plusDays(2), 1, 20.0, CurrencyEnum.USD);

        // When - Call the save method
        Optional<Price> result = priceService.update(priceCommand);

        // Then - Verify that the result is correct
        assertEquals(Optional.empty(), result);


    }
}
