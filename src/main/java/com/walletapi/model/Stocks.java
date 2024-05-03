package com.walletapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "stocks", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ticker")
    private String ticker;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Companies companies;

    @Column(name = "free_float")
    private BigDecimal freeFloat;

    @Column(name = "tag_along")
    private BigDecimal tagAlong;

    @Column(name = "avg_daily_liquidity")
    private BigInteger avgDailyLiquidity;
}
