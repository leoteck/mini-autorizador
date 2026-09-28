package com.vr.authorizer.strategy;

import com.vr.authorizer.service.TransacaoResultado;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(2)
class SenhaInvalidaStrategy implements TransacaoStrategy {

    @Override
    public Optional<TransacaoResultado> processar(TransacaoContext context) {
        return context.cartao()
                .filter(cartao -> !cartao.getSenha().equals(context.senha()))
                .map(cartao -> TransacaoResultado.SENHA_INVALIDA);
    }
}
