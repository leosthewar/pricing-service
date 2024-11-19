package com.capitole.pricingservice.application.port.in.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Unit tests for the PriceQuery.
 * <p>
 * This class includes test cases to verify the behavior of the PriceQuery class,
 * ensuring that the throws an exception when the required fields are not provided
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
class PriceQueryTest {

    @Test
    public void testPriceQuery_ok() {
        Integer brandId = 1;
        Long productId = 2L;
        PriceQuery priceQuery = new PriceQuery( LocalDateTime.now(), brandId, productId);
        assertNotNull(priceQuery);
    }

    @Test
    public void testPriceQuery_applicationTimeIsNull() {
        Integer brandId = 1;
        Long productId = 2L;

        assertThrows(ConstraintViolationException.class, () -> new PriceQuery( null, brandId, productId));
    }
    @Test
    public void testPriceQuery_brandIdIsNull() {
        Long productId = 2L;

        assertThrows(ConstraintViolationException.class, () -> new PriceQuery( LocalDateTime.now(), null, productId));
    }
    @Test
    public void testPriceQuery_productIdIsNull() {
        Integer brandId = 1;
        assertThrows(ConstraintViolationException.class, () -> new PriceQuery( LocalDateTime.now(), brandId, null));

    }


}