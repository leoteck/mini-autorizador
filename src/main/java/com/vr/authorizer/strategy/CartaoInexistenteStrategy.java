package com.vr.authorizer.strategy;

import com.vr.authorizer.service.TransacaoResultado;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
class CartaoInexistenteStrategy implements TransacaoStrategy {

    @Override
    public Optional<TransacaoResultado> processar(TransacaoContext context) {
        return context.cartao().isEmpty()
                ? Optional.of(TransacaoResultado.CARTAO_INEXISTENTE)
                : Optional.empty();
    }
}
