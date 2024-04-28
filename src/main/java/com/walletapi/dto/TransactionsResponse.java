package com.walletapi.dto;

import com.walletapi.model.Stocks;
import com.walletapi.model.Wallets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@AllArgsConstructor
public class TransactionsResponse {
    private Integer id;
    private Wallets wallets;
    private Stocks stocks;
    private BigDecimal price;
    private Date date;
    private Integer amount;
    private String operation;
}
