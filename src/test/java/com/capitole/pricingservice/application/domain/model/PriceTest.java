package com.capitole.pricingservice.application.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PriceTest {

    @Test
    public void testConstructorWithAllFields() {
        Long id = 1L;
        Integer brandId = 1;
        Long productId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        Integer priceList = 1;
        Double price = 10.0;
        CurrencyEnum currency = CurrencyEnum.EUR;
        Integer priority = 1;

        Price priceObject = new Price(id, brandId, productId, startDate, endDate, priceList, price, currency, priority);

        assertEquals(id, priceObject.getId());
        assertEquals(brandId, priceObject.getBrandId());
        assertEquals(productId, priceObject.getProductId());
        assertEquals(startDate, priceObject.getStartDate());
        assertEquals(endDate, priceObject.getEndDate());
        assertEquals(priceList, priceObject.getPriceList());
        assertEquals(price, priceObject.getPrice());
        assertEquals(currency, priceObject.getCurrency());
        assertEquals(priority, priceObject.getPriority());
    }

    @Test
    public void testConstructorWithDefaultIdAndPriority() {
        Integer brandId = 1;
        Long productId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        Integer priceList = 1;
        Double price = 10.0;
        CurrencyEnum currency = CurrencyEnum.EUR;

        Price priceObject = new Price(brandId, productId, startDate, endDate, priceList, price, currency);

        assertNull(priceObject.getId());
        assertEquals(brandId, priceObject.getBrandId());
        assertEquals(productId, priceObject.getProductId());
        assertEquals(startDate, priceObject.getStartDate());
        assertEquals(endDate, priceObject.getEndDate());
        assertEquals(priceList, priceObject.getPriceList());
        assertEquals(price, priceObject.getPrice());
        assertEquals(currency, priceObject.getCurrency());
        assertEquals(0, priceObject.getPriority().intValue());
    }

    @Test
    public void testConstructorWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Price(null, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR, 1));

    }

    @Test
    public void testConstructorWithNullBrandId() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, null, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( null, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNullProductId() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, null, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price(1, null, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price(1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, null));
    }

    @Test
    public void testConstructorWithNullStartDate() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, null, LocalDateTime.now(), 1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( 1, 1L, null, LocalDateTime.now(), 1, 10.0, CurrencyEnum.EUR));
    }
    @Test
    public void testConstructorWithNullEndDate() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(),null, 1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( 1, 1L, LocalDateTime.now(), null, 1, 10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithStartDateAfterEndDate() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( 1, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1, 10.0, CurrencyEnum.EUR));
    }


    @Test
    public void testConstructorWithNullPriceList() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNegativePriceList() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), -1, 10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price( 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), -1, 10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNullPrice() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, null, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price(1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, null, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, -10.0, CurrencyEnum.EUR, 1));
        assertThrows(IllegalArgumentException.class, () -> new Price(1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, -10.0, CurrencyEnum.EUR));
    }

    @Test
    public void testConstructorWithNullPriority() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR, null));
    }

    @Test
    public void testConstructorWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> new Price(1L, 1, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10.0, CurrencyEnum.EUR, -1));
    }
}