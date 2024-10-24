package com.capitole.pricingservice.adapter.out.jpa.repository;

import com.capitole.pricingservice.application.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Integration tests for the PriceJpaAdapter.
 * <p>
 * This class includes test cases to verify the behavior of the PriceJpaAdapter methods,
 * ensuring that the correct results are returned under various scenarios. It uses the
 * Spring Boot test framework to perform the tests.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
@DataJpaTest
@Import({PriceJpaAdapter.class})
@TestPropertySource(properties = "spring.sql.init.mode=never")
class PriceJpaAdapterTest {

    @Autowired
    private PriceJpaAdapter priceJpaAdapter;

    @Test
    void getPriceByBrandIdAndProductIdAndApplicationTime_priceNotExists() {
        Optional<Price> price = priceJpaAdapter.getPriceByBrandIdAndProductIdAndApplicationTime(1, 1122L, LocalDateTime.of(2020, 7, 14, 0, 0, 0));
        assertTrue(price.isEmpty());
    }

    @Test
    @Sql(scripts = "classpath:/jpa-adapter-test-data.sql")
    void getPriceByBrandIdAndProductIdAndApplicationTime_priceExists() {
        Optional<Price> price = priceJpaAdapter.getPriceByBrandIdAndProductIdAndApplicationTime(1, 35455L, LocalDateTime.of(2020, 7, 14, 0, 0, 0));

        assertTrue(price.isPresent());
        assertEquals(price.get().getPrice(), 10.50);

    }
}