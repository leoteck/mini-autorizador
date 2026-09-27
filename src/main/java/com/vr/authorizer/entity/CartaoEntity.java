package com.vr.authorizer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CARTAO")
public class CartaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARTAO")
    private Long id;

    @Column(name = "NUMERO_CARTAO", nullable = false, unique = true, length = 16)
    private String numeroCartao;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "SALDO_ID", nullable = false, unique = true)
    private SaldoEntity saldo;

    public CartaoEntity(String numeroCartao, String senha, SaldoEntity saldo) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
        this.saldo = saldo;
    }
}
