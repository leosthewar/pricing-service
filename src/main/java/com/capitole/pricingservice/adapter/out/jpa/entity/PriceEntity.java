package com.capitole.pricingservice.adapter.out.jpa.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class PriceEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BRAND_ID")
    private Integer brandId;
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Column(name = "PRICE_LIST")
    private Integer priceList;
    @Column(name = "PRIORITY")
    private Integer priority;
    @Column(name = "START_DATE")
    private LocalDateTime startDate;
    @Column(name = "END_DATE")
    private LocalDateTime endDate;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "CURR")
    private String currency;

    public PriceEntity(Long id, Integer brandId, Long productId, Integer priceList, Integer prority,
                       LocalDateTime startDate, LocalDateTime endDate, Double price, String currency) {
        this.id = id;
        this.brandId = brandId;
        this.productId = productId;
        this.priceList = priceList;
        this.priority = prority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.currency = currency;
    }

}
