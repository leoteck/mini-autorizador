package com.vr.authorizer.service;

public enum TransacaoResultado {
    AUTORIZADA("OK"),
    SALDO_INSUFICIENTE("SALDO_INSUFICIENTE"),
    SENHA_INVALIDA("SENHA_INVALIDA"),
    CARTAO_INEXISTENTE("CARTAO_INEXISTENTE");

    private final String mensagem;

    TransacaoResultado(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
