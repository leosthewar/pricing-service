package com.capitole.pricingservice.adapter.out.jpa.repository;

import com.capitole.pricingservice.adapter.out.jpa.entity.PriceEntityMapper;
import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.port.out.PriceRepository;
import com.capitole.pricingservice.common.annotation.JpaAdapter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
/**
 * Implementation of the {@link PriceRepository} port using a JPA repository.
 *
 * <p>This class uses Spring Data JPA to query the database and retrieve the price for a given brand and product
 * at a specific application time.
 *
 * <p>It implements the {@link PriceRepository} port.out interface and is annotated with the {@link JpaAdapter} annotation.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 * @see PriceRepository
 * @see JpaAdapter
 * @see PriceJpaRepository
 */
@RequiredArgsConstructor
@JpaAdapter
class PriceJpaAdapter implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;

    /**
     * Retrieves the price for a given brand and product at a specific application time.
     *
     * @param brandId the identifier of the brand
     * @param productId the identifier of the product
     * @param applicationDate the date and time for which the price is requested
     * @return an Optional containing the Price if found, or an empty Optional if not
     */
    @Override
    public Optional<Price> getPriceByBrandIdAndProductIdAndApplicationTime(Integer brandId, Long productId, LocalDateTime applicationDate) {

        return priceJpaRepository.findTop1ByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByProrityDesc(brandId,productId,applicationDate,applicationDate).
                map(PriceEntityMapper::toPrice);
    }
}
