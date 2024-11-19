package com.capitole.pricingservice.application.domain.model;


import java.util.HashMap;
import java.util.Map;

public enum CurrencyEnum {
    USD("USD"),
    EUR("EUR"),
    COP("COP");

    // Getter
    private final String code;

    // Constructor
    CurrencyEnum(String code) {
        this.code = code;
    }

    // Static map for fast lookup
    private static final Map<String, CurrencyEnum> CODE_TO_ENUM = new HashMap<>();

    static {
        for (CurrencyEnum currency : values()) {
            CODE_TO_ENUM.put(currency.code.toUpperCase(), currency);
        }
    }

    // Static method to safely retrieve an enum instance
    public static CurrencyEnum fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Currency code must not be null or empty");
        }
        CurrencyEnum currency = CODE_TO_ENUM.get(code.toUpperCase());
        if (currency == null) {
            throw new IllegalArgumentException("No matching currency for code: " + code);
        }
        return currency;
    }
}
