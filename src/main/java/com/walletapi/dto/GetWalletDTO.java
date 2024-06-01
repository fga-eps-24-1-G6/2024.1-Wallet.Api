package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetWalletDTO {
    private String name;
    private String externalId;
    private List<WalletItem> stocks;
    private BigDecimal totalValue;
    private BigDecimal variation;
}
