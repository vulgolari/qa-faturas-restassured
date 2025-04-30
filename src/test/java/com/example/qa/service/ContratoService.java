package com.example.qa.service;

import com.example.qa.model.Contrato;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ContratoService {
    
    public Response criarContrato(Contrato contrato) {
        return given()
                .body(contrato)
                .when()
                .post("/contratos");
    }
    
    public Response buscarFaturasPorContrato(Long contratoId) {
        return given()
                .queryParam("contrato_id", contratoId)
                .when()
                .get("/faturas");
    }
    
    public Response cancelarContrato(Long contratoId) {
        return given()
                .pathParam("id", contratoId)
                .when()
                .post("/contratos/{id}/cancelar");
    }
} 