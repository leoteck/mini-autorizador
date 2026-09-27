package com.vr.authorizer.service;

import com.vr.authorizer.entity.CartaoEntity;
import com.vr.authorizer.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public TransacaoResultado realizar(String numeroCartao, String senha, BigDecimal valor) {
        CartaoEntity cartao = transacaoRepository.lockByNumeroCartao(numeroCartao)
                .orElse(null);

        if (cartao == null) {
            return TransacaoResultado.CARTAO_INEXISTENTE;
        }
        if (!cartao.getSenha().equals(senha)) {
            return TransacaoResultado.SENHA_INVALIDA;
        }

        if (cartao.getSaldo().compareTo(valor) < 0) {
            return TransacaoResultado.SALDO_INSUFICIENTE;
        }

        cartao.setSaldo(cartao.getSaldo().subtract(valor));
        return TransacaoResultado.AUTORIZADA;
    }
}
