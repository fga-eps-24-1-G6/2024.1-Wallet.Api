package com.walletapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StocksDTO {
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
}