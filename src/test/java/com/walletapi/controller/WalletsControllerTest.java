package com.walletapi.controller;

import com.walletapi.service.MockMvcService;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
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
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(("Carteira Teste")))
                .andExpect(jsonPath("$.externalId").value(("123")));
    }

    @Test
    @Order(2)
    void shouldGetAllWallets() throws Exception {
        mockMvcService.get("/api/wallets/get")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(3)
    void shouldGetWalletsById() throws Exception {
        mockMvcService.get("/api/wallets/get/1")
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(4)
    void shouldNotGetWalletsById() throws Exception {
        mockMvcService.get("/api/wallets/get/5")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)));
    }

    @Test
    @Order(5)
    void shouldUpdateWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/wallets/update/1")
                        .content("{"
                                + "\"name\": \"Carteira Teste Atualizada\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Carteira Teste Atualizada"));
    }

    @Test
    @Order(6)
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
                .andExpect(jsonPath("$.errorCode").value((404)));
    }

    @Test
    @Order(7)
    void shouldDeleteWallets() throws Exception {
        mockMvcService.delete("/api/wallets/delete/1")
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(8)
    void shouldNotDeleteWallets() throws Exception {
        mockMvcService.delete("/api/wallets/delete/3")
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value((404)));
    }

}
