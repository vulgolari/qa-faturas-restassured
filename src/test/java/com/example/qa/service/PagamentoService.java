package com.example.qa.service;

import com.example.qa.model.Pagamento;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class PagamentoService {
    
    public Response processarPagamento(Pagamento pagamento) {
        return given()
                .body(pagamento)
                .when()
                .post("/pagamentos");
    }
    
    public Response consultarStatusPagamento(Long pagamentoId) {
        return given()
                .pathParam("id", pagamentoId)
                .when()
                .get("/pagamentos/{id}");
    }
    
    public Response consultarPagamentosPorFatura(Long faturaId) {
        return given()
                .queryParam("fatura_id", faturaId)
                .when()
                .get("/pagamentos");
    }
} 