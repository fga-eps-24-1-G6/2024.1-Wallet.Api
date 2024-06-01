package com.walletapi.service;

import com.walletapi.dto.*;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Transactions;
import com.walletapi.repository.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionsServiceTest {

    @InjectMocks
    private TransactionsService transactionsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private StocksService stocksService;

    @Mock
    private WalletsService walletsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() throws ParseException {
        String dateString = "2023-05-27";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        TransactionsDTO transactionDTO = new TransactionsDTO(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");
        Transactions transaction = new Transactions(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");

        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transaction);
        when(walletsService.getWalletById(anyInt())).thenReturn(new GetWalletDTO("Wallet 1", "123", Collections.emptyList(), BigDecimal.ZERO, BigDecimal.ZERO));
        when(stocksService.getStocksByTicker(anyString())).thenReturn(new StocksDTO("ABEV3", BigDecimal.valueOf(12.37), "AMBEV", BigDecimal.valueOf(27.89), BigDecimal.valueOf(80.0), BigInteger.valueOf(326266000), "SMALL", BigDecimal.valueOf(0.3), BigDecimal.valueOf(3.0), BigDecimal.valueOf(-12.37)));

        TransactionsResponse response = transactionsService.createTransaction(transactionDTO);

        assertEquals(transaction.getId(), response.getId());
        assertEquals(transaction.getTicker(), response.getStocks().getTicker());
    }

    @Test
    void testGetTransactionById() throws ParseException {
        String dateString = "2023-05-27";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Transactions transaction = new Transactions(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");

        when(transactionsRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletsService.getWalletById(anyInt())).thenReturn(new GetWalletDTO("Wallet 1", "123", Collections.emptyList(), BigDecimal.ZERO, BigDecimal.ZERO));
        when(stocksService.getStocksByTicker(anyString())).thenReturn(new StocksDTO("ABEV3", BigDecimal.valueOf(12.37), "AMBEV", BigDecimal.valueOf(27.89), BigDecimal.valueOf(80.0), BigInteger.valueOf(326266000), "SMALL", BigDecimal.valueOf(0.3), BigDecimal.valueOf(3.0), BigDecimal.valueOf(-12.37)));

        TransactionsResponse response = transactionsService.getTransactionById(1);

        assertEquals(transaction.getId(), response.getId());
        assertEquals(transaction.getTicker(), response.getStocks().getTicker());
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionsRepository.findById(1)).thenReturn(Optional.empty());

        BadRequestNotFoundException exception = assertThrows(BadRequestNotFoundException.class, () -> {
            transactionsService.getTransactionById(1);
        });

        assertEquals("Lançamento não encontrado com o ID: 1", exception.getMessage());
    }

    @Test
    void testGetTransactionsByWalletId() throws ParseException {
        String dateString = "2023-05-27";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Transactions transaction1 = new Transactions(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");
        Transactions transaction2 = new Transactions(2, 1, "ITSA4", BigDecimal.valueOf(12.51), date, 10, "COMPRA");

        when(transactionsRepository.getTransactionsByWalletId(1)).thenReturn(Optional.of(Arrays.asList(transaction1, transaction2)));
        when(walletsService.getWalletById(anyInt())).thenReturn(new GetWalletDTO("Wallet 1", "123", Collections.emptyList(), BigDecimal.ZERO, BigDecimal.ZERO));
        when(stocksService.getStocksByTicker(anyString()))
                .thenReturn(new StocksDTO("ABEV3", BigDecimal.valueOf(12.37), "AMBEV", BigDecimal.valueOf(27.89), BigDecimal.valueOf(80.0), BigInteger.valueOf(326266000), "SMALL", BigDecimal.valueOf(0.3), BigDecimal.valueOf(3.0), BigDecimal.valueOf(-12.37)))
                .thenReturn(new StocksDTO("ITSA4", BigDecimal.valueOf(12.37), "ITSA4", BigDecimal.valueOf(27.89), BigDecimal.valueOf(80.0), BigInteger.valueOf(326266000), "SMALL", BigDecimal.valueOf(0.3), BigDecimal.valueOf(3.0), BigDecimal.valueOf(-12.37)));

        TransactionsWalletResponse response = transactionsService.getTransactionsByWalletId(1);

        assertEquals(2, response.getTransactions().size());
    }

    @Test
    void testGetTransactionsByWalletIdNotFound() {
        when(transactionsRepository.getTransactionsByWalletId(1)).thenReturn(Optional.empty());

        BadRequestNotFoundException exception = assertThrows(BadRequestNotFoundException.class, () -> {
            transactionsService.getTransactionsByWalletId(1);
        });

        assertEquals("Lançamentos não encontrados para o walletId: 1", exception.getMessage());
    }

    @Test
    void testUpdateTransaction() throws ParseException {
        String dateString = "2023-05-27";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Transactions transaction = new Transactions(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");
        TransactionsDTO transactionDTO = new TransactionsDTO(1, 1, "ABEV3", BigDecimal.valueOf(13.09), date, 10, "COMPRA");

        when(transactionsRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transaction);

        TransactionsDTO response = transactionsService.updateTransaction(1, transactionDTO);

        assertEquals(transaction.getId(), response.getId());
        assertEquals(transactionDTO.getPrice(), response.getPrice());
    }

    @Test
    void testDeleteTransaction() throws ParseException {
        String dateString = "2023-05-27";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Transactions transaction = new Transactions(1, 1, "ABEV3", BigDecimal.valueOf(12.51), date, 10, "COMPRA");

        when(transactionsRepository.findById(1)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionsRepository).delete(transaction);

        transactionsService.deleteTransaction(1);

        verify(transactionsRepository, times(1)).delete(transaction);
    }

    @Test
    void testDeleteTransactionNotFound() {
        when(transactionsRepository.findById(1)).thenReturn(Optional.empty());

        BadRequestNotFoundException exception = assertThrows(BadRequestNotFoundException.class, () -> {
            transactionsService.deleteTransaction(1);
        });

        assertEquals("Transação não encontrada com o ID: 1", exception.getMessage());
    }
}
