package com.vr.authorizer.controller.dto;

import jakarta.validation.constraints.NotNull;

public record CartaoRequest(
        @NotNull String numeroCartao,
        @NotNull String senha) {
}
