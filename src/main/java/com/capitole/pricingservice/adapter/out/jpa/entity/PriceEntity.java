package com.capitole.pricingservice.adapter.out.jpa.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PriceEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BRAND_ID")
    private Integer brandId;
    @Column(name= "PRODUCT_ID")
    private Long productId;
    @Column(name= "PRICE_LIST")
    private Integer priceList;
    @Column(name= "PRIORITY")
    private Integer prority;
    @Column(name= "START_DATE")
    private LocalDateTime startDate;
    @Column(name= "END_DATE")
    private LocalDateTime endDate;
    @Column(name= "PRICE")
    private Double price;
    @Column(name= "CURR")
    private String currency;


}
