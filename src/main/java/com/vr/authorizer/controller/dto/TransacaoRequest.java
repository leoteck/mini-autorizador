package com.vr.authorizer.controller.dto;

import java.math.BigDecimal;

public record TransacaoRequest(String numeroCartao, String senhaCartao, BigDecimal valor) {
}
