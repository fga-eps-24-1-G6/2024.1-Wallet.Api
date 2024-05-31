package com.walletapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "stocks", schema = "public")
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

    public Stocks(Integer id, String ticker, Companies companies, BigDecimal freeFloat, BigDecimal tagAlong, BigInteger avgDailyLiquidity) {
        this.id = id;
        this.ticker = ticker;
        this.companies = companies;
        this.freeFloat = freeFloat;
        this.tagAlong = tagAlong;
        this.avgDailyLiquidity = avgDailyLiquidity;
    }

    public Stocks() {
    }

    public Integer getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public Companies getCompanies() {
        return companies;
    }

    public BigDecimal getFreeFloat() {
        return freeFloat;
    }

    public BigDecimal getTagAlong() {
        return tagAlong;
    }

    public BigInteger getAvgDailyLiquidity() {
        return avgDailyLiquidity;
    }
}
