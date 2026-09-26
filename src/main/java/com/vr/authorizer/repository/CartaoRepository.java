package com.vr.authorizer.repository;

import com.vr.authorizer.entity.CartaoEntity;

import java.util.Optional;

public interface CartaoRepository {

    public Optional<CartaoEntity> findByNumeroCartao(final String numeroCartao);

    public CartaoEntity save(CartaoEntity cartao);
}
