package com.vr.authorizer.controller;

import com.vr.authorizer.controller.dto.CartaoRequest;
import com.vr.authorizer.controller.dto.CartaoResponse;
import com.vr.authorizer.exception.CartaoJaExistenteException;
import com.vr.authorizer.service.CartaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private static final Logger logger = LoggerFactory.getLogger(CartaoController.class);

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping
    public ResponseEntity<CartaoResponse> criar(@RequestBody CartaoRequest request) {
        CartaoResponse response;
        try {
            response = cartaoService.criar(request.numeroCartao(), request.senha());
        }catch(CartaoJaExistenteException ex) {
            logger.warn("Erro ao criar cartão: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new CartaoResponse(request.numeroCartao(), request.senha()));
        }
        logger.info("Cartão criado com sucesso!");
        return ResponseEntity.status(response != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        return cartaoService.obterSaldo(numeroCartao)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
