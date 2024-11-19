package com.capitole.pricingservice.application.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyEnumTest {

    @Test
    void testFromCode_ValidCode_ReturnsCurrencyEnum() {
        CurrencyEnum currency = CurrencyEnum.fromCode("USD");
        assertEquals(CurrencyEnum.USD, currency);
    }

    @Test
    void testFromCode_InvalidCode_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CurrencyEnum.fromCode("INVALID"));
    }

    @Test
    void testFromCode_NullCode_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CurrencyEnum.fromCode(null));
    }

    @Test
    void testFromCode_EmptyCode_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CurrencyEnum.fromCode(""));
    }
}