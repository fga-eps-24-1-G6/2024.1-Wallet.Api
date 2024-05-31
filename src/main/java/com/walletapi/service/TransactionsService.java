package com.walletapi.service;

import com.walletapi.dto.*;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Prices;
import com.walletapi.model.Transactions;
import com.walletapi.repository.PriceRepository;
import com.walletapi.repository.TransactionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final StocksService stocksService;
    private final WalletsService walletsService;
    private final PriceRepository priceRepository;

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

    public TransactionsDTO updateTransaction(Integer id, TransactionsDTO data) {
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Transação não encontrada com o ID: " + id));

        transaction.setWalletId(data.getWalletId());
        transaction.setTicker(data.getTicker());
        transaction.setPrice(data.getPrice());
        transaction.setDate(data.getDate());
        transaction.setAmount(data.getAmount());
        transaction.setOperation(data.getOperation());

        Transactions updatedTransaction = transactionsRepository.save(transaction);

        return TransactionsDTO.builder()
                .id(updatedTransaction.getId())
                .walletId(updatedTransaction.getWalletId())
                .ticker(updatedTransaction.getTicker())
                .price(updatedTransaction.getPrice())
                .date(updatedTransaction.getDate())
                .amount(updatedTransaction.getAmount())
                .operation(updatedTransaction.getOperation())
                .build();
    }

    public void deleteTransaction(Integer id) {
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Transação não encontrada com o ID: " + id));
        transactionsRepository.delete(transaction);
    }

    public List<ProfitabilityDTO> getProfitabilityByWalletId(Integer walletId) {
        // Obtendo todas as transações para a carteira fornecida
        List<Transactions> allTransactions = transactionsRepository.getTransactionsByWalletId(walletId)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Lançamentos não encontrados para o walletId: " + walletId));

        // Coletando todos os tickers únicos presentes nas transações
        Set<String> uniqueTickers = allTransactions.stream()
                .map(Transactions::getTicker)
                .collect(Collectors.toSet());

        // Buscando todas as ações relacionadas aos tickers únicos
        List<StocksDTO> stocks = uniqueTickers.stream()
                .map(stocksService::getStocksByTicker)
                .collect(Collectors.toList());

        // Agrupando transações por mês
        Map<String, List<Transactions>> transactionsByMonth = allTransactions.stream()
                .collect(Collectors.groupingBy(transaction -> {
                    LocalDate date = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
                }));

        List<ProfitabilityDTO> profitabilityList = new ArrayList<>();
        BigDecimal cumulativeApplied = BigDecimal.ZERO;
        BigDecimal cumulativeGains = BigDecimal.ZERO;

        for (Map.Entry<String, List<Transactions>> entry : transactionsByMonth.entrySet()) {
            String month = entry.getKey();
            List<Transactions> transactionsInMonth = entry.getValue();

            // Calculando o total aplicado (somente compras) no mês atual
            BigDecimal sumApplied = transactionsInMonth.stream()
                    .filter(transaction -> "COMPRA".equalsIgnoreCase(transaction.getOperation()))
                    .map(transaction -> transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getAmount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculando os ganhos (somente vendas) no mês atual
            BigDecimal sumGains = transactionsInMonth.stream()
                    .filter(transaction -> "VENDA".equalsIgnoreCase(transaction.getOperation()))
                    .map(transaction -> transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getAmount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cumulativeApplied = cumulativeApplied.add(sumApplied);
            cumulativeGains = cumulativeGains.add(sumGains);

            BigDecimal currentValuation = BigDecimal.ZERO;
            for (String ticker : uniqueTickers) {
                StocksDTO stock = stocks.stream()
                        .filter(s -> s.getTicker().equals(ticker))
                        .findFirst()
                        .orElse(null);

                if (stock != null) {
                    // Obter o último preço do mês de cada ação
                    List<Prices> prices = priceRepository.findAllByStockIdIdOrderByPriceDateDesc(stock.getId());

                    BigDecimal lastPriceOfMonth = prices.stream()
                            .filter(price -> isLastDayOfMonth(price.getPriceDate()))
                            .map(Prices::getValue)
                            .reduce((first, second) -> second) // Obtendo o último preço do mês
                            .orElse(BigDecimal.ZERO);

                    long totalAmount = allTransactions.stream()
                            .filter(transaction -> transaction.getTicker().equals(ticker))
                            .filter(transaction -> "COMPRA".equalsIgnoreCase(transaction.getOperation()))
                            .mapToLong(Transactions::getAmount)
                            .sum();

                    currentValuation = currentValuation.add(lastPriceOfMonth.multiply(BigDecimal.valueOf(totalAmount)));
                }
            }

            BigDecimal sumTotal = cumulativeApplied.add(cumulativeGains).add(currentValuation);

            BigDecimal profitability = BigDecimal.ZERO;
            if (!cumulativeApplied.equals(BigDecimal.ZERO)) {
                profitability = cumulativeGains.subtract(cumulativeApplied).divide(cumulativeApplied, 2, RoundingMode.HALF_UP);
            }

            profitabilityList.add(new ProfitabilityDTO(month, cumulativeApplied, cumulativeGains, sumTotal, profitability));
        }

        return profitabilityList;
    }

    private boolean isLastDayOfMonth(LocalDate date) {
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

}
