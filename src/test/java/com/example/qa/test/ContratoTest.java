package com.example.qa.test;

import com.example.qa.base.BaseTest;
import com.example.qa.model.Contrato;
import com.example.qa.service.ContratoService;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContratoTest extends BaseTest {
    
    private final ContratoService contratoService = new ContratoService();
    
    @Test
    public void deveCriarContratoComSucesso() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        // Act
        Response response = contratoService.criarContrato(contrato);
        
        // Assert
        response.then()
                .statusCode(201)
                .body("id", notNullValue());
    }
    
    @Test
    public void deveGerarFaturasAoCriarContrato() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        // Act
        Response responseCriacao = contratoService.criarContrato(contrato);
        Long contratoId = responseCriacao.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFaturas.then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("contratoId", everyItem(equalTo(contratoId.intValue())));
    }
    
    @Test
    public void naoDeveCriarContratoComDataInicioPosteriorDataTermino() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now().plusYears(1));
        contrato.setDataTermino(LocalDate.now());
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        // Act
        Response response = contratoService.criarContrato(contrato);
        
        // Assert
        response.then()
                .statusCode(400);
    }
} 