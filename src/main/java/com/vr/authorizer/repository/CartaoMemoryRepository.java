package com.vr.authorizer.repository;

import com.vr.authorizer.entity.CartaoEntity;
import com.vr.authorizer.service.TransacaoResultado;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class CartaoMemoryRepository implements CartaoRepository {

    private final ConcurrentMap<String, CartaoEntity> cartoes = new ConcurrentHashMap<>();

    public CartaoEntity save(CartaoEntity cartao) {
        cartoes.putIfAbsent(cartao.getNumeroCartao(), cartao);
        return cartao;
    }

    public Optional<CartaoEntity> findByNumeroCartao(String numeroCartao) {
        return Optional.ofNullable(cartoes.get(numeroCartao));
    }

    public TransacaoResultado debitar(String numeroCartao, String senha, BigDecimal valor) {
        AtomicReference<TransacaoResultado> resultado = new AtomicReference<>();

        cartoes.compute(numeroCartao, (chave, cartao) -> {
            if (cartao == null) {
                resultado.set(TransacaoResultado.CARTAO_INEXISTENTE);
                return null;
            }
            if (!cartao.getSenha().equals(senha)) {
                resultado.set(TransacaoResultado.SENHA_INVALIDA);
                return cartao;
            }
            if (cartao.getSaldo().getValor().compareTo(valor) < 0) {
                resultado.set(TransacaoResultado.SALDO_INSUFICIENTE);
                return cartao;
            }

            resultado.set(TransacaoResultado.AUTORIZADA);
            return debitar(valor);
        });

        return resultado.get();
    }

    private CartaoEntity debitar(final BigDecimal valor) {
        return null;
    }

}
