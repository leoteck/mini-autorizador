package com.vr.authorizer.converter;

import com.vr.authorizer.controller.dto.CartaoRequest;
import com.vr.authorizer.controller.dto.CartaoResponse;
import com.vr.authorizer.entity.CartaoEntity;

public class CartaoConverter {

    public static CartaoResponse convertEntityToResponse(final CartaoEntity cartaoEntity) {
        return new CartaoResponse(cartaoEntity.getSenha(), cartaoEntity.getNumeroCartao());
    }
}
