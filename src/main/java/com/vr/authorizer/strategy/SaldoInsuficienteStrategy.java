package com.vr.authorizer.strategy;

import com.vr.authorizer.service.TransacaoResultado;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(3)
class SaldoInsuficienteStrategy implements TransacaoStrategy {

    @Override
    public Optional<TransacaoResultado> processar(TransacaoContext context) {
        return context.cartao()
                .filter(cartao -> cartao.getSaldo().compareTo(context.valor()) < 0)
                .map(cartao -> TransacaoResultado.SALDO_INSUFICIENTE);
    }
}
