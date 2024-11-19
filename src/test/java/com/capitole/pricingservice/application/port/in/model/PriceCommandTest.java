package com.capitole.pricingservice.application.port.in.model;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PriceCommandTest {

    @Test
    public void testValidPriceCommand() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        PriceCreateCommand priceCommand = new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency);
        assertNotNull(priceCommand);
    }

    @Test
    public void testNullBrandId() {
        // Given
        Integer brandId = null;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullProductId() {
        // Given
        Integer brandId = 1;
        Long productId = null;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullStartDate() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = null;
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullEndDate() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = null;
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullPriceList() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = null;
        Double price = 10.99;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullPrice() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = null;
        CurrencyEnum currency = CurrencyEnum.EUR;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }

    @Test
    public void testNullCurrency() {
        // Given
        Integer brandId = 1;
        Long productId = 2L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Integer priceList = 3;
        Double price = 10.99;
        CurrencyEnum currency = null;

        // When and Then
        assertThrows(ConstraintViolationException.class, () -> new PriceCreateCommand(brandId, productId, startDate, endDate, priceList, price, currency));
    }
}