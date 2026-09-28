package com.vr.authorizer.strategy;

import com.vr.authorizer.entity.CartaoEntity;

import java.math.BigDecimal;
import java.util.Optional;

public record TransacaoContext(Optional<CartaoEntity> cartao, String senha, BigDecimal valor) {
}
