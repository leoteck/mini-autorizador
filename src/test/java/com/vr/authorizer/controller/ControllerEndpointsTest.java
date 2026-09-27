package com.vr.authorizer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerEndpointsTest {

    private static final String NUMERO_CARTAO = "9" + String.format(
            "%015d",
            Math.floorMod(UUID.randomUUID().getLeastSignificantBits(), 1_000_000_000_000_000L));

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHonorCardAndTransactionContracts() throws Exception {
        String cartaoRequest = """
                {"numeroCartao":"%s","senha":"1234"}
                """.formatted(NUMERO_CARTAO);

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartaoRequest))
                .andExpect(status().isCreated())
                .andExpect(content().json(cartaoRequest));

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartaoRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(cartaoRequest));

        mockMvc.perform(get("/cartoes/{numeroCartao}", NUMERO_CARTAO))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));

        mockMvc.perform(get("/cartoes/{numeroCartao}", "0000000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroCartao":"%s","senhaCartao":"1234","valor":499.99}
                                """.formatted(NUMERO_CARTAO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));

        mockMvc.perform(get("/cartoes/{numeroCartao}", NUMERO_CARTAO))
                .andExpect(status().isOk())
                .andExpect(content().string("0.01"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroCartao":"%s","senhaCartao":"1234","valor":0.02}
                                """.formatted(NUMERO_CARTAO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroCartao":"%s","senhaCartao":"9999","valor":0.01}
                                """.formatted(NUMERO_CARTAO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroCartao":"0000000000000000","senhaCartao":"1234","valor":1.00}
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }
}
