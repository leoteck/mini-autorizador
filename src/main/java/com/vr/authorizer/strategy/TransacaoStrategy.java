package com.vr.authorizer.strategy;

import com.vr.authorizer.service.TransacaoResultado;

import java.util.Optional;

public interface TransacaoStrategy {
    Optional<TransacaoResultado> processar(TransacaoContext context);
}
