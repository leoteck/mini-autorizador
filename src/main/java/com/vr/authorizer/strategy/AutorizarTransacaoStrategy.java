package com.vr.authorizer.strategy;

import com.vr.authorizer.entity.CartaoEntity;
import com.vr.authorizer.service.TransacaoResultado;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(4)
class AutorizarTransacaoStrategy implements TransacaoStrategy {

    @Override
    public Optional<TransacaoResultado> processar(TransacaoContext context) {
        CartaoEntity cartao = context.cartao()
                .orElseThrow(() -> new IllegalStateException("Não é possível autorizar uma transação sem cartão"));

        cartao.setSaldo(cartao.getSaldo().subtract(context.valor()));
        return Optional.of(TransacaoResultado.AUTORIZADA);
    }
}
