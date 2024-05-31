package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProfitabilityDTO {
    private String month;
    private BigDecimal sumApplied;
    private BigDecimal sumGains;
    private BigDecimal sumTotal;
    private BigDecimal profitability;
}