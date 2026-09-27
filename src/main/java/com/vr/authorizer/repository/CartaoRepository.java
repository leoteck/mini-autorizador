package com.vr.authorizer.repository;

import com.vr.authorizer.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<CartaoEntity, Long> {

    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao);
}
