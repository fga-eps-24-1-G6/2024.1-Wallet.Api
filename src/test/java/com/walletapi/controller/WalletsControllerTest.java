package com.walletapi.controller;

import com.walletapi.service.MockMvcService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletsControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvcService mockMvcService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldCreateWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/wallets/create")
                        .content("{"
                                + "\"name\": \"Carteira Teste\","
                                + "\"externalId\": \"123\""
                                + "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value((1)))
                .andExpect(jsonPath("$.name").value(("Carteira Teste")))
                .andExpect(jsonPath("$.externalId").value(("123")));
    }

    @Test
    @Order(2)
    void shouldNotCreateWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/wallets/create")
                        .content("{"
                                + "\"name\": \"Carteira Teste De novo\","
                                + "\"externalId\": \"123\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((409)))
                .andExpect(jsonPath("$.message").value(("Fail to create the wallet")));
    }

    @Test
    @Order(3)
    void shouldGetAllWallets() throws Exception {
        mockMvcService.get("/api/wallets/get")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Carteira Principal - Lucas"))
                .andExpect(jsonPath("$[0].externalId").value(1))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Carteira Teste"))
                .andExpect(jsonPath("$[1].externalId").value(123));
    }

    @Test
    @Order(4)
    void shouldGetWalletsById() throws Exception {
        mockMvcService.get("/api/wallets/get/2")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Carteira Principal - Lucas"))
                .andExpect(jsonPath("$.externalId").value(1));
    }

    @Test
    @Order(5)
    void shouldNotGetWalletsById() throws Exception {
        mockMvcService.get("/api/wallets/get/3")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Carteira não encontrada com o ID: 3")));
    }

    @Test
    @Order(6)
    void shouldUpdateWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/wallets/update/1")
                        .content("{"
                                + "\"name\": \"Carteira Teste Atualizada\","
                                + "\"externalId\": \"123\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Carteira Teste Atualizada"))
                .andExpect(jsonPath("$.externalId").value(123));
    }

    @Test
    @Order(7)
    void shouldNotUpdateWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/wallets/update/3")
                        .content("{"
                                + "\"name\": \"Carteira Teste De novo Atualizada\","
                                + "\"externalId\": \"123\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Carteira não encontrada com o ID: 3")));
    }

    @Test
    @Order(8)
    void shouldDeleteWallets() throws Exception {
        mockMvcService.delete("/api/wallets/delete/1")
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(9)
    void shouldNotDeleteWallets() throws Exception {
        mockMvcService.delete("/api/wallets/delete/1")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)))
                .andExpect(jsonPath("$.message").value(("Carteira não encontrada com o ID: 1")));
    }

}
