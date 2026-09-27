package com.vr.authorizer.repository;

import com.vr.authorizer.entity.CartaoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransacaoRepository extends Repository<CartaoEntity, Long> {

    @Query(value = """
            SELECT *
            FROM CARTAO
            WHERE NUMERO_CARTAO = :numeroCartao
            FOR UPDATE
            """, nativeQuery = true)
    Optional<CartaoEntity> lockByNumeroCartao(@Param("numeroCartao") String numeroCartao);
}
