package com.vr.authorizer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartaoEntity {

    private String numeroCartao;
    private String senha;
    private SaldoEntity saldo;

}
