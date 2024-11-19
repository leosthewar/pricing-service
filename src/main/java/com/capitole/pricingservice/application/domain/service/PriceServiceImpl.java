package com.capitole.pricingservice.application.domain.service;

import com.capitole.pricingservice.application.domain.model.Price;
import com.capitole.pricingservice.application.domain.model.PriceSummary;
import com.capitole.pricingservice.application.port.in.PriceService;
import com.capitole.pricingservice.application.port.in.model.PriceCreateCommand;
import com.capitole.pricingservice.application.port.in.model.PriceMapper;
import com.capitole.pricingservice.application.port.in.model.PriceQuery;
import com.capitole.pricingservice.application.port.in.model.PriceUpdateCommand;
import com.capitole.pricingservice.application.port.out.PriceRepository;
import com.capitole.pricingservice.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the {@link PriceService} port using a JPA repository.
 *
 * <p>This class uses{@link PriceRepository} to query the database and retrieve the price for a given brand and product
 * at a specific application time.
 *
 * <p>It implements the {@link PriceService} port.in interface and is annotated with the {@link UseCase} annotation.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 * @see PriceService
 * @see UseCase
 * @see PriceRepository
 */
@RequiredArgsConstructor
@UseCase
class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceSummary> getPriceByApplicationTimeBrandIdProductId(PriceQuery priceQuery) {
        // validate business rules
        return priceRepository.getPriceByBrandIdAndProductIdAndApplicationTime(priceQuery.brandId(), priceQuery.productId(), priceQuery.applicationTime());

    }

    @Override
    @Transactional
    public Price save(PriceCreateCommand price) {
        // validate business rules
        if (price.startDate()
                 .isAfter(price.endDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        return priceRepository.save(PriceMapper.toPriceToCreateWithDefaultPriority(price));
    }

    @Override
    @Transactional
    public Optional<Price> update(PriceUpdateCommand priceCommand) {
        Optional<Price> priceOp = priceRepository.findById(priceCommand.id());
        if (priceOp.isPresent()) {
            Price priceToUpdate = new Price(priceOp.get().getId(), priceCommand.brandId(), priceCommand.productId(),
                    priceCommand.startDate(), priceCommand.endDate(), priceCommand.priceList(),
                    priceCommand.price(), priceCommand.currency(), priceOp.get().getPriority()
            );
            return Optional.ofNullable(priceRepository.save(priceToUpdate));
        }
        return Optional.empty();
    }

}
