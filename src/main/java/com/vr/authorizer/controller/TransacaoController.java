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

import java.util.Map;

import static com.vr.authorizer.service.TransacaoResultado.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    private static final Map<TransacaoResultado, HttpStatus> STATUS_POR_RESULTADO = Map.of(
            AUTORIZADA, HttpStatus.CREATED,
            SALDO_INSUFICIENTE, HttpStatus.UNPROCESSABLE_ENTITY,
            SENHA_INVALIDA, HttpStatus.UNPROCESSABLE_ENTITY,
            CARTAO_INEXISTENTE, HttpStatus.UNPROCESSABLE_ENTITY
    );

    @PostMapping
    public ResponseEntity<String> realizar(@RequestBody TransacaoRequest request) {
        TransacaoResultado resultado = transacaoService.realizar(
                request.numeroCartao(), request.senhaCartao(), request.valor());

        return buildResponse(resultado);
    }

    private ResponseEntity<String> buildResponse(final TransacaoResultado resultado) {
        HttpStatus status = STATUS_POR_RESULTADO.get(resultado);
        return ResponseEntity.status(status).body(resultado.getMensagem());
    }
}
