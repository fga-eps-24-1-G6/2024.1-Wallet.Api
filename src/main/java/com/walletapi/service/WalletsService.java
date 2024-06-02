package com.walletapi.service;

import com.walletapi.dto.CreateWalletDTO;
import com.walletapi.dto.GetWalletDTO;
import com.walletapi.dto.GetWalletsDTO;
import com.walletapi.dto.StocksDTO;
import com.walletapi.dto.WalletItem;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Transactions;
import com.walletapi.model.Wallets;
import com.walletapi.repository.TransactionsRepository;
import com.walletapi.repository.WalletsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WalletsService {

    private final WalletsRepository walletsRepository;
    private final TransactionsRepository transactionsRepository;
    private final StocksService stocksService;

    public CreateWalletDTO createWallet(String name, String externalId) {
        try {
            Wallets savedWallet = walletsRepository.save(Wallets.builder()
                    .name(name)
                    .externalId(externalId)
                    .build());

            return CreateWalletDTO.builder()
                    .id(savedWallet.getId())
                    .name(savedWallet.getName())
                    .externalId(savedWallet.getExternalId())
                    .build();
        } catch (Exception e) {
            throw new BadRequestNotFoundException(409, "Fail to create the wallet");
        }
    }

    public List<GetWalletsDTO> getAllWallets() {
        List<Wallets> wallets = walletsRepository.findAll();
        return wallets.stream()
                .map(wallet -> GetWalletsDTO.builder()
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .externalId(wallet.getExternalId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<GetWalletsDTO> getWalletsByExternalId(String externalId) {
        List<Wallets> wallets = walletsRepository.findByExternalId(externalId)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + externalId));

        return wallets.stream()
                .map(wallet -> GetWalletsDTO.builder()
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .externalId(wallet.getExternalId())
                        .build())
                .collect(Collectors.toList());
    }

    public GetWalletDTO getWalletById(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        List<Transactions> transactions = transactionsRepository.getTransactionsByWalletId(wallet.getId())
                .orElse(Collections.emptyList());

        BigDecimal walletValue = BigDecimal.ZERO;
        BigDecimal walletVariation = BigDecimal.ZERO;
        List<WalletItem> walletStocks = Collections.emptyList();

        if(!transactions.isEmpty()){
            List<Object> walletData = getWalletData(transactions);
            walletValue = (BigDecimal) walletData.get(0);
            walletVariation = (BigDecimal) walletData.get(1);
            walletStocks = (List<WalletItem>) walletData.get(2);
        }

        return GetWalletDTO.builder()
                .name(wallet.getName())
                .externalId(wallet.getExternalId())
                .stocks(walletStocks)
                .totalValue(walletValue)
                .variation(walletVariation)
                .build();
    }

    private List<Object> getWalletData(List<Transactions> transactions) {
        Map<String, Map<String, Object>> stockBalances = new HashMap<>();
        BigDecimal amountSpent = BigDecimal.ZERO;

        for (Transactions transaction : transactions) {
            String ticker = transaction.getTicker();
            Integer amount = transaction.getAmount();
            String operation = transaction.getOperation();
            BigDecimal price = transaction.getPrice();

            Map<String, Object> stockData = stockBalances.getOrDefault(ticker, new HashMap<>());
            stockData.putIfAbsent("amount", 0);
            stockData.putIfAbsent("amount_bought", 0);
            stockData.putIfAbsent("total_cost", BigDecimal.ZERO);

            BigDecimal operationValue = price.multiply(BigDecimal.valueOf(amount));

            if (operation.equals("COMPRA")) {
                stockData.put("amount", (Integer) stockData.get("amount") + amount);
                stockData.put("amount_bought", (Integer) stockData.get("amount_bought") + amount);

                stockData.put("total_cost", ((BigDecimal) stockData.get("total_cost")).add(operationValue));
                amountSpent = amountSpent.add(operationValue);
            } else if (operation.equals("VENDA")) {
                amountSpent = amountSpent.subtract(operationValue);

                Integer newAmount = Math.max(0, (Integer) stockData.get("amount") - amount);
                stockData.put("amount", newAmount);
                if(newAmount.equals(0)){
                    stockData.put("total_cost", BigDecimal.ZERO);
                    stockData.put("amount_bought", 0);
                }
            }

            stockBalances.put(ticker, stockData);
        }

        List<WalletItem> currentStocks = new ArrayList<>();
        BigDecimal walletValue = BigDecimal.ZERO;

        for (Map.Entry<String, Map<String, Object>> entry : stockBalances.entrySet()) {
            String ticker = entry.getKey();
            Map<String, Object> stockData = entry.getValue();
            int amount = (Integer) stockData.get("amount");
            int amountBought = (Integer) stockData.get("amount_bought");
            BigDecimal totalCost = (BigDecimal) stockData.get("total_cost");

            if (amount > 0) {
                StocksDTO stock = stocksService.getStocksByTicker(ticker);
                walletValue = walletValue.add(stock.getCurrentPrice().multiply(BigDecimal.valueOf(amount)));

                BigDecimal avgPrice = totalCost.divide(BigDecimal.valueOf(amountBought),2, RoundingMode.HALF_UP);
                BigDecimal variation = (stock.getCurrentPrice().subtract(avgPrice)).divide(avgPrice, 4, RoundingMode.HALF_UP);

                WalletItem currentStock = WalletItem.builder()
                        .ticker(ticker)
                        .amount(amount)
                        .avgPrice(avgPrice)
                        .variation(variation)
                        .build();
                currentStocks.add(currentStock);
            }
        }

        BigDecimal walletVariation = (walletValue.subtract(amountSpent)).divide(amountSpent, 4, RoundingMode.HALF_UP);
        List<Object> result = new ArrayList<>();
        result.add(walletValue);
        result.add(walletVariation);
        result.add(currentStocks);

        return result;
    }

    public GetWalletDTO updateWallet(Integer id, String name) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        wallet.setName(name);
        Wallets updatedWallet = walletsRepository.save(wallet);

        return GetWalletDTO.builder()
                .name(updatedWallet.getName())
                .externalId(updatedWallet.getExternalId())
                .build();
    }

    public void deleteWallet(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));
        walletsRepository.delete(wallet);
    }
}
