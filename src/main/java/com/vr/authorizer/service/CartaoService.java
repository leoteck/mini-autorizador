package com.vr.authorizer.service;

import com.vr.authorizer.controller.dto.CartaoResponse;
import com.vr.authorizer.converter.CartaoConverter;
import com.vr.authorizer.entity.CartaoEntity;
import com.vr.authorizer.entity.SaldoEntity;
import com.vr.authorizer.exception.CartaoJaExistenteException;
import com.vr.authorizer.repository.CartaoMemoryRepository;
import com.vr.authorizer.repository.CartaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {

    private static final BigDecimal SALDO_INICIAL = new BigDecimal("500.00");

    private final CartaoRepository cartaoRepository;

    public CartaoService(CartaoMemoryRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    //@Transaction
    public Optional<BigDecimal> obterSaldo(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(CartaoEntity::getSaldo)
                .map(SaldoEntity::getValor);
    }

    public CartaoResponse criar(String numeroCartao, String senha) {
       cartaoRepository.findByNumeroCartao(numeroCartao).ifPresent(
               cartao -> { throw new CartaoJaExistenteException(numeroCartao); });

        final CartaoEntity cartaoEntity = cartaoRepository.save(new CartaoEntity(numeroCartao, senha, new SaldoEntity(SALDO_INICIAL)));
       return CartaoConverter.convertEntityToResponse(cartaoEntity);
    }
}
