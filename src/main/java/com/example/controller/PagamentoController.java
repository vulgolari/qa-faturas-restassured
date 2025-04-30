package com.example.controller;

import com.example.model.Pagamento;
import com.example.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contratos/{contratoId}/faturas/{faturaId}/pagamentos")
public class PagamentoController {
    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Map<String, String>> processarPagamento(
            @PathVariable String contratoId,
            @PathVariable String faturaId,
            @RequestBody Map<String, Object> request) {

        double valor = Double.parseDouble(request.get("valor_pago").toString());
        String metodoPagamento = request.get("metodo_pagamento").toString();

        Pagamento pagamento = pagamentoService.processarPagamento(contratoId, faturaId, valor, metodoPagamento);

        return ResponseEntity.ok(Map.of("status", "PAGA"));
    }
} 