package com.example.service;

import com.example.model.Contrato;
import com.example.model.Fatura;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;

import java.util.List;

public class ContratoService {
    private static final String BASE_URL = "http://localhost:8089";

    public ContratoService() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response criarContrato(Contrato contrato) {
        if (contrato == null) {
            throw new IllegalArgumentException("Contrato não pode ser nulo");
        }

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(contrato)
                .when()
                .post("/contratos")
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(201), Matchers.is(200)))
                .extract()
                .response();
    }

    public List<Fatura> buscarFaturasPorContrato(String contratoId) {
        if (contratoId == null || contratoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do contrato não pode ser nulo ou vazio");
        }

        return RestAssured.given()
                .when()
                .get("/contratos/" + contratoId + "/faturas")
                .then()
                .statusCode(200)
                .body("$", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .getList(".", Fatura.class);
    }

    public Response processarPagamento(String contratoId, String faturaId, double valor, String metodoPagamento) {
        if (contratoId == null || contratoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do contrato não pode ser nulo ou vazio");
        }
        if (faturaId == null || faturaId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da fatura não pode ser nulo ou vazio");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser maior que zero");
        }
        if (metodoPagamento == null || metodoPagamento.trim().isEmpty()) {
            throw new IllegalArgumentException("Método de pagamento não pode ser nulo ou vazio");
        }

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"contrato_id\": \"%s\", \"fatura_id\": \"%s\", \"valor_pago\": %.2f, \"metodo_pagamento\": \"%s\"}",
                        contratoId, faturaId, valor, metodoPagamento))
                .when()
                .post("/contratos/" + contratoId + "/faturas/" + faturaId + "/pagamentos")
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(200), Matchers.is(201)))
                .extract()
                .response();
    }
} 