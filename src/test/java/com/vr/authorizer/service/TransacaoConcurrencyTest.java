package com.vr.authorizer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransacaoConcurrencyTest {

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    void shouldNotAuthorizeTwoConcurrentDebitsAgainstTheSameBalance() throws Exception {
        String numeroCartao = "8" + String.format(
                "%015d",
                Math.floorMod(UUID.randomUUID().getLeastSignificantBits(), 1_000_000_000_000_000L));
        cartaoService.criar(numeroCartao, "1234");

        var executor = Executors.newFixedThreadPool(2);
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try {
            Callable<TransacaoResultado> transaction = () -> {
                ready.countDown();
                assertTrue(start.await(5, TimeUnit.SECONDS));
                return transacaoService.realizar(numeroCartao, "1234", new BigDecimal("300.00"));
            };
            Future<TransacaoResultado> first = executor.submit(transaction);
            Future<TransacaoResultado> second = executor.submit(transaction);

            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();

            TransacaoResultado firstResult = first.get(10, TimeUnit.SECONDS);
            TransacaoResultado secondResult = second.get(10, TimeUnit.SECONDS);
            assertEquals(1, List.of(firstResult, secondResult).stream()
                    .filter(result -> result == TransacaoResultado.AUTORIZADA)
                    .count());
            assertEquals(1, List.of(firstResult, secondResult).stream()
                    .filter(result -> result == TransacaoResultado.SALDO_INSUFICIENTE)
                    .count());
            assertEquals(new BigDecimal("200.00"), cartaoService.obterSaldo(numeroCartao).orElseThrow());
        } finally {
            executor.shutdownNow();
        }
    }
}
