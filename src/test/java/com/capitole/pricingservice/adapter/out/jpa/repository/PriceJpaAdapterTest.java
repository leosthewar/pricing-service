package com.capitole.pricingservice.adapter.out.jpa.repository;

import com.capitole.pricingservice.application.domain.model.CurrencyEnum;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // to reset the DB after each test
class PriceJpaAdapterTest {

    @Autowired
    private PriceJpaAdapter priceJpaAdapter;

    @Test
    void getPriceByBrandIdAndProductIdAndApplicationTime_priceNotExists() {
        Optional<PriceSummary> price = priceJpaAdapter.getPriceByBrandIdAndProductIdAndApplicationTime(1, 1122L, LocalDateTime.of(2020, 7, 14, 0, 0, 0));
        assertTrue(price.isEmpty());
    }

    @Test
    @Sql(scripts = "classpath:/jpa-adapter-test-data.sql")
    void getPriceByBrandIdAndProductIdAndApplicationTime_priceExists() {
        Optional<PriceSummary> price = priceJpaAdapter.getPriceByBrandIdAndProductIdAndApplicationTime(1, 35455L, LocalDateTime.of(2020, 7, 14, 0, 0, 0));

        assertTrue(price.isPresent());
        assertEquals(price.get()
                          .price(), 10.50);
    }

    @Test
    void save_creation_ok() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Price price = new Price(1122, 1234L, dateTime, dateTime.plusDays(2), 1, 10.0, CurrencyEnum.EUR);
        Price priceExpected = new Price(1L, 1122, 1234L, dateTime, dateTime.plusDays(2), 1, 10.0, CurrencyEnum.EUR, 0);
        Price priceSaved = priceJpaAdapter.save(price);
        assertEquals(priceExpected, priceSaved);
    }


    @Test
    void save_update_ok() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Price price = new Price(1L, 1122, 1234L, dateTime, dateTime.plusDays(2), 1, 10.0, CurrencyEnum.EUR, 2);
        Price priceExpected = new Price(1L, 1122, 1234L, dateTime, dateTime.plusDays(2), 1, 10.0, CurrencyEnum.EUR, 2);
        Price priceSaved = priceJpaAdapter.save(price);
        assertEquals(priceExpected, priceSaved);
    }

    @Test
    @Sql(scripts = "classpath:/jpa-adapter-test-data.sql")
    void findById_ok() {
        Optional<Price> price = priceJpaAdapter.findById(33L);
        Price expected = new Price(33L, 1, 35455L, LocalDateTime.of(2020, 6, 14, 0, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), 1, 10.50, CurrencyEnum.EUR, 0);
        assertTrue(price.isPresent());
        assertEquals(expected, price.get());
    }

    @Test
    @Sql(scripts = "classpath:/jpa-adapter-test-data.sql")
    void findById_priceNotExists() {
        Optional<Price> price = priceJpaAdapter.findById(1L);
        assertTrue(price.isEmpty());
    }

}