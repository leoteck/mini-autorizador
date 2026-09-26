package com.vr.authorizer.exception;

public class CartaoJaExistenteException extends RuntimeException {

    public CartaoJaExistenteException(final String numeroCartao) {
        super( "Já existe um cadastrado para o cartão: "+numeroCartao);
    }
}
