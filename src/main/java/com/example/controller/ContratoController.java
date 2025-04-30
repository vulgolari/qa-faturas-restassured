package com.example.controller;

import com.example.model.Contrato;
import com.example.model.Fatura;
import com.example.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contratos")
public class ContratoController {
    @Autowired
    private ContratoService contratoService;

    @PostMapping
    public ResponseEntity<Map<String, String>> criarContrato(@RequestBody Contrato contrato) {
        Contrato contratoSalvo = contratoService.criarContrato(contrato);
        return ResponseEntity.status(201)
                .body(Map.of("contrato_id", contratoSalvo.getId().toString()));
    }

    @GetMapping("/{contratoId}/faturas")
    public ResponseEntity<List<Fatura>> buscarFaturasPorContrato(@PathVariable String contratoId) {
        List<Fatura> faturas = contratoService.buscarFaturasPorContrato(contratoId);
        return ResponseEntity.ok(faturas);
    }
} 