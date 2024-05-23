package com.walletapi.controller;

import com.walletapi.service.MockMvcService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TransactionsControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvcService mockMvcService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldCreateTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/transactions/create")
                        .content("{"
                                + "\"walletId\": 2,"
                                + "\"ticker\": \"petr4\","
                                + "\"price\": 12.50,"
                                + "\"date\": \"2024-05-13\","
                                + "\"amount\": 1,"
                                + "\"operation\": \"COMPRA\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value((1)))
                .andExpect(jsonPath("$.wallets.id").value((2)))
                .andExpect(jsonPath("$.wallets.name").value(("Carteira Principal - Lucas")))
                .andExpect(jsonPath("$.wallets.externalId").value(("1")))
                .andExpect(jsonPath("$.stocks.ticker").value(("petr4")))
                .andExpect(jsonPath("$.stocks.currentPrice").value((27.96)))
                .andExpect(jsonPath("$.stocks.companyName").value(("PETROLEO BRASILEIRO S.A. PETROBRAS")))
                .andExpect(jsonPath("$.stocks.freeFloat").value((63.33)))
                .andExpect(jsonPath("$.stocks.tagAlong").value((100)))
                .andExpect(jsonPath("$.stocks.avgDailyLiquidity").value((1617781000)))
                .andExpect(jsonPath("$.stocks.categorie").value(("SMALL")))
                .andExpect(jsonPath("$.stocks.variationOneDay").value((0.1700)))
                .andExpect(jsonPath("$.stocks.variationOneMonth").value((16.3400)))
                .andExpect(jsonPath("$.stocks.variationTwelveMonths").value((30.9800)))
                .andExpect(jsonPath("$.price").value((12.50)))
                .andExpect(jsonPath("$.date").value(("2024-05-13T00:00:00.000+00:00")))
                .andExpect(jsonPath("$.amount").value((1)))
                .andExpect(jsonPath("$.operation").value(("COMPRA")));
    }

    @Test
    @Order(2)
    void shouldNotCreateTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/transactions/create")
                        .content("{"
                                + "\"walletId\": 2,"
                                + "\"ticker\": \"4rtep\","
                                + "\"price\": 12.50,"
                                + "\"date\": \"2024-05-13\","
                                + "\"amount\": 1,"
                                + "\"operation\": \"COMPRA\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((409)))
                .andExpect(jsonPath("$.message").value(("Fail to create the transaction")));
    }

    @Test
    @Order(3)
    void shouldGetTransactionById() throws Exception {
        mockMvcService.get("/api/transactions/get/1")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value((1)))
                .andExpect(jsonPath("$.wallets.id").value((2)))
                .andExpect(jsonPath("$.wallets.name").value(("Carteira Principal - Lucas")))
                .andExpect(jsonPath("$.wallets.externalId").value(("1")))
                .andExpect(jsonPath("$.stocks.ticker").value(("petr4")))
                .andExpect(jsonPath("$.stocks.currentPrice").value((27.96)))
                .andExpect(jsonPath("$.stocks.companyName").value(("PETROLEO BRASILEIRO S.A. PETROBRAS")))
                .andExpect(jsonPath("$.stocks.freeFloat").value((63.33)))
                .andExpect(jsonPath("$.stocks.tagAlong").value((100)))
                .andExpect(jsonPath("$.stocks.avgDailyLiquidity").value((1617781000)))
                .andExpect(jsonPath("$.stocks.categorie").value(("SMALL")))
                .andExpect(jsonPath("$.stocks.variationOneDay").value((0.1700)))
                .andExpect(jsonPath("$.stocks.variationOneMonth").value((16.3400)))
                .andExpect(jsonPath("$.stocks.variationTwelveMonths").value((30.9800)))
                .andExpect(jsonPath("$.price").value((12.50)))
                .andExpect(jsonPath("$.date").value(("2024-05-13T00:00:00.000+00:00")))
                .andExpect(jsonPath("$.amount").value((1)))
                .andExpect(jsonPath("$.operation").value(("COMPRA")));
    }

    @Test
    @Order(4)
    void shouldNotGetTransactionById() throws Exception {
        mockMvcService.get("/api/transactions/get/2")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Lançamento não encontrado com o ID: 2")));
    }

    @Test
    @Order(5)
    void shouldGetTransactionsByWalletId() throws Exception {
        mockMvcService.get("/api/transactions/get/wallet/2")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.wallet.id").value((2)))
                .andExpect(jsonPath("$.wallet.name").value(("Carteira Principal - Lucas")))
                .andExpect(jsonPath("$.wallet.externalId").value(("1")))
                .andExpect(jsonPath("$.transactions[0].id").value(("1")))
                .andExpect(jsonPath("$.transactions[0].stocks.ticker").value(("petr4")))
                .andExpect(jsonPath("$.transactions[0].stocks.currentPrice").value((27.96)))
                .andExpect(jsonPath("$.transactions[0].stocks.freeFloat").value((63.33)))
                .andExpect(jsonPath("$.transactions[0].stocks.companyName").value(("PETROLEO BRASILEIRO S.A. PETROBRAS")))
                .andExpect(jsonPath("$.transactions[0].stocks.tagAlong").value((100)))
                .andExpect(jsonPath("$.transactions[0].stocks.avgDailyLiquidity").value((1617781000)))
                .andExpect(jsonPath("$.transactions[0].stocks.categorie").value(("SMALL")))
                .andExpect(jsonPath("$.transactions[0].stocks.variationOneDay").value((0.1700)))
                .andExpect(jsonPath("$.transactions[0].stocks.variationOneMonth").value((16.3400)))
                .andExpect(jsonPath("$.transactions[0].stocks.variationTwelveMonths").value((30.9800)))
                .andExpect(jsonPath("$.transactions[0].price").value((12.50)))
                .andExpect(jsonPath("$.transactions[0].date").value(("2024-05-13T00:00:00.000+00:00")))
                .andExpect(jsonPath("$.transactions[0].amount").value((1)))
                .andExpect(jsonPath("$.transactions[0].operation").value(("COMPRA")));
    }

    @Test
    @Order(6)
    void shouldNotGetTransactionsByWalletId() throws Exception {
        mockMvcService.get("/api/transactions/get/wallet/1")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Carteira não encontrada com o ID: 1")));
    }

    @Test
    @Order(7)
    void shouldUpdateTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/transactions/update/1")
                        .content("{"
                                + "\"walletId\": 2,"
                                + "\"ticker\": \"petr4\","
                                + "\"price\": 1.00,"
                                + "\"date\": \"2023-01-06\","
                                + "\"amount\": 12,"
                                + "\"operation\": \"VENDA\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value((1)))
                .andExpect(jsonPath("$.walletId").value((2)))
                .andExpect(jsonPath("$.ticker").value(("petr4")))
                .andExpect(jsonPath("$.price").value((1.00)))
                .andExpect(jsonPath("$.date").value(("2023-01-06T00:00:00.000+00:00")))
                .andExpect(jsonPath("$.amount").value((12)))
                .andExpect(jsonPath("$.operation").value(("VENDA")));
    }

    @Test
    @Order(8)
    void shouldNotUpdateTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/transactions/update/2")
                        .content("{"
                                + "\"walletId\": 2,"
                                + "\"ticker\": \"petr4\","
                                + "\"price\": 1.00,"
                                + "\"date\": \"2023-01-06\","
                                + "\"amount\": 12,"
                                + "\"operation\": \"VENDA\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Transação não encontrada com o ID: 2")));
    }

    @Test
    @Order(9)
    void shouldDeleteTransaction() throws Exception {
        mockMvcService.delete("/api/transactions/delete/1")
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(10)
    void shouldNotDeleteTransaction() throws Exception {
        mockMvcService.delete("/api/transactions/delete/1")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Transação não encontrada com o ID: 1")));
    }
}
