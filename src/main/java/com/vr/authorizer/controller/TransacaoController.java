package com.vr.authorizer.controller;

import com.vr.authorizer.controller.dto.TransacaoRequest;
import com.vr.authorizer.service.TransacaoResultado;
import com.vr.authorizer.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<String> realizar(@RequestBody TransacaoRequest request) {
        TransacaoResultado resultado = transacaoService.realizar(
                request.numeroCartao(), request.senhaCartao(), request.valor());

        if (resultado == TransacaoResultado.AUTORIZADA) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado.getMensagem());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resultado.getMensagem());
    }
}
