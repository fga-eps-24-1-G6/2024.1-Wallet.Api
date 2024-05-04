package com.walletapi.service;

import com.walletapi.dto.*;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Transactions;
import com.walletapi.model.Wallets;
import com.walletapi.repository.TransactionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final StocksService stocksService;
    private final WalletsService walletsService;

    public TransactionsResponse createTransaction(TransactionsDTO data) {
        try {
            Transactions savedTransaction = transactionsRepository.save(Transactions.builder()
                    .walletId(data.getWalletId())
                    .ticker(data.getTicker())
                    .price(data.getPrice())
                    .date(data.getDate())
                    .amount(data.getAmount())
                    .operation(data.getOperation())
                    .build());

            // getWalletById
            WalletsDTO wallets = walletsService.getWalletById(data.getWalletId());
            // getStocksByTicker
            StocksDTO stocks = stocksService.getStocksByTicker(data.getTicker());

            return TransactionsResponse.builder()
                    .id(savedTransaction.getId())
                    .wallets(wallets)
                    .stocks(stocks)
                    .price(savedTransaction.getPrice())
                    .date(savedTransaction.getDate())
                    .amount(savedTransaction.getAmount())
                    .operation(savedTransaction.getOperation())
                    .build();
        } catch (Exception e) {
            throw new BadRequestNotFoundException(409, "Fail to create the transaction");
        }
    }

    public TransactionsResponse getTransactionById(Integer id) {
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Lançamento não encontrado com o ID: " + id));

        // getWalletById
        WalletsDTO wallets = walletsService.getWalletById(transaction.getWalletId());
        // getStocksByTicker
        StocksDTO stocks = stocksService.getStocksByTicker(transaction.getTicker());

        return TransactionsResponse.builder()
                .id(transaction.getId())
                .wallets(wallets)
                .stocks(stocks)
                .price(transaction.getPrice())
                .date(transaction.getDate())
                .amount(transaction.getAmount())
                .operation(transaction.getOperation())
                .build();
    }

    public TransactionsWalletResponse getTransactionsByWalletId(Integer walletId) {
        List<Transactions> transactions = transactionsRepository.getTransactionsByWalletId(walletId)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Lançamentos não encontrados para o walletId: " + walletId));

        // getWalletById
        WalletsDTO wallet = walletsService.getWalletById(walletId);

        List<TransactionsSimpleResponse> transactionsResponses = new ArrayList<>();

        for (Transactions transaction : transactions) {
            // getStocksByTicker
            StocksDTO stocks = stocksService.getStocksByTicker(transaction.getTicker());

            TransactionsSimpleResponse response = TransactionsSimpleResponse.builder()
                    .id(transaction.getId())
                    .stocks(stocks)
                    .price(transaction.getPrice())
                    .date(transaction.getDate())
                    .amount(transaction.getAmount())
                    .operation(transaction.getOperation())
                    .build();

            transactionsResponses.add(response);
        }

        return TransactionsWalletResponse.builder()
                .wallet(wallet)
                .transactions(transactionsResponses)
                .build();
    }
}
