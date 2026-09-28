package com.vr.authorizer.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransacaoRequest(
        @NotNull String numeroCartao,
        @NotNull String senhaCartao,
        @NotNull BigDecimal valor) {
}
