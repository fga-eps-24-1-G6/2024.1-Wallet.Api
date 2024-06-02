package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class TransactionsResponse {
    private Integer id;
    private GetWalletDTO wallets;
    private StocksDTO stocks;
    private BigDecimal price;
    private Date date;
    private Integer amount;
    private String operation;
}
