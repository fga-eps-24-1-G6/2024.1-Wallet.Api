package com.walletapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StocksDTO {
    private Integer id;
    private String ticker;
    private BigDecimal currentPrice;
    private String companyName;
    private BigDecimal freeFloat;
    private BigDecimal tagAlong;
    private BigInteger avgDailyLiquidity;
    private String categorie;
    private BigDecimal variationOneDay;
    private BigDecimal variationOneMonth;
    private BigDecimal variationTwelveMonths;

    public StocksDTO(String ticker, BigDecimal currentPrice, String companyName, BigDecimal freeFloat, BigDecimal tagAlong, BigInteger avgDailyLiquidity, String categorie, BigDecimal variationOneDay, BigDecimal variationOneMonth, BigDecimal variationTwelveMonths) {
        this.avgDailyLiquidity = avgDailyLiquidity;
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.companyName = companyName;
        this.freeFloat = freeFloat;
        this.tagAlong = tagAlong;
        this.variationOneDay = variationOneDay;
        this.variationOneMonth = variationOneMonth;
        this.variationTwelveMonths = variationTwelveMonths;
        this.categorie = categorie;
    }
}