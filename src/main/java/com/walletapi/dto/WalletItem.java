package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class WalletItem {
    private String ticker;
    private Integer amount;
    private BigDecimal avgPrice;
    private BigDecimal variation;
}
