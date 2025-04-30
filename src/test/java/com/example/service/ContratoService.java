package com.example.service;

import com.example.model.Contrato;
import com.example.model.Fatura;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

public class ContratoService {
    private static final String BASE_URL = "http://localhost:8080";

    public ContratoService() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response criarContrato(Contrato contrato) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(contrato)
                .when()
                .post("/contratos");
    }

    public List<Fatura> buscarFaturasPorContrato(String contratoId) {
        return RestAssured.given()
                .when()
                .get("/contratos/" + contratoId + "/faturas")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Fatura.class);
    }

    public Response processarPagamento(String contratoId, String faturaId, double valor, String metodoPagamento) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"contrato_id\": \"%s\", \"fatura_id\": \"%s\", \"valor_pago\": %f, \"metodo_pagamento\": \"%s\"}",
                        contratoId, faturaId, valor, metodoPagamento))
                .when()
                .post("/contratos/" + contratoId + "/faturas/" + faturaId + "/pagamentos");
    }
} 