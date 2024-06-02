package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TransactionsWalletResponse {
    private GetWalletDTO wallet;
    private List<TransactionsSimpleResponse> transactions;
}
