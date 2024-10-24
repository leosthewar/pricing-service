package com.capitole.pricingservice.adapter.out.jpa.repository;

import com.capitole.pricingservice.adapter.out.jpa.entity.PriceEntity;
import com.capitole.pricingservice.adapter.out.jpa.entity.PriceSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {


    /**
     * Finds the top 1 price for the given brandId, productId and date range, ordered by priority in descending order.
     *
     * @param brandId the brand id
     * @param productId the product id
     * @param startDate the start date of the date range
     * @param endDate the end date of the date range
     * @return the top 1 price summary
     */
    Optional<PriceSummaryProjection> findTop1ByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByProrityDesc(Integer brandId, Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
