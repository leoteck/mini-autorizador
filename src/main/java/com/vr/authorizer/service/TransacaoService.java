package com.vr.authorizer.service;

import com.vr.authorizer.repository.TransacaoRepository;
import com.vr.authorizer.strategy.TransacaoContext;
import com.vr.authorizer.strategy.TransacaoStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final List<TransacaoStrategy> strategies;

    public TransacaoService(
            TransacaoRepository transacaoRepository,
            List<TransacaoStrategy> strategies) {
        this.transacaoRepository = transacaoRepository;
        this.strategies = strategies;
    }

    /**
     * Este metodo realiza a transação debitando o valor do cartão se houver saldo.
     * Foi utilizado o padrão Strategy na tentativa de elimiar os ifs. Porém, acho que não ficou uma boa solução
     * pois caso surja nova regra a ser adicionada, será necessário além de implementar uma nova estratégia,
     * verificar também a ordem das estratégias, pois a ordem de execução é importante.
     **/
    @Transactional
    public TransacaoResultado realizar(String numeroCartao, String senha, BigDecimal valor) {
        final TransacaoContext context = new TransacaoContext(
                transacaoRepository.lockByNumeroCartao(numeroCartao),
                senha,
                valor);

        return strategies.stream()
                .map(strategy -> strategy.processar(context))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Nenhuma estratégia concluiu o processamento da transação"));
    }
}
