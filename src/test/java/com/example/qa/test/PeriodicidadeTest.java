package com.example.qa.test;

import com.example.qa.base.BaseTest;
import com.example.qa.model.Contrato;
import com.example.qa.service.ContratoService;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de Periodicidade de Faturas")
public class PeriodicidadeTest extends BaseTest {
    
    private final ContratoService contratoService = new ContratoService();
    
    @Test
    @DisplayName("Deve gerar faturas mensais corretamente")
    public void deveGerarFaturasMensaisCorretamente() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        // Act
        Response responseContrato = contratoService.criarContrato(contrato);
        Long contratoId = responseContrato.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFaturas.then()
                .statusCode(200)
                .body("size()", equalTo(12)) // 12 faturas mensais
                .body("contratoId", everyItem(equalTo(contratoId.intValue())))
                .body("valor", everyItem(equalTo(100.00f))); // 1200/12 = 100
    }
    
    @Test
    @DisplayName("Deve gerar faturas trimestrais corretamente")
    public void deveGerarFaturasTrimestraisCorretamente() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("TRIMESTRAL");
        
        // Act
        Response responseContrato = contratoService.criarContrato(contrato);
        Long contratoId = responseContrato.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFaturas.then()
                .statusCode(200)
                .body("size()", equalTo(4)) // 4 faturas trimestrais
                .body("contratoId", everyItem(equalTo(contratoId.intValue())))
                .body("valor", everyItem(equalTo(300.00f))); // 1200/4 = 300
    }
    
    @Test
    @DisplayName("Deve gerar faturas anuais corretamente")
    public void deveGerarFaturasAnuaisCorretamente() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("ANUAL");
        
        // Act
        Response responseContrato = contratoService.criarContrato(contrato);
        Long contratoId = responseContrato.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFaturas.then()
                .statusCode(200)
                .body("size()", equalTo(1)) // 1 fatura anual
                .body("contratoId", everyItem(equalTo(contratoId.intValue())))
                .body("valor", everyItem(equalTo(1200.00f))); // Valor total
    }
    
    @Test
    @DisplayName("Não deve aceitar periodicidade inválida")
    public void naoDeveAceitarPeriodicidadeInvalida() {
        // Arrange
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("INVALIDO");
        
        // Act
        Response response = contratoService.criarContrato(contrato);
        
        // Assert
        response.then()
                .statusCode(400)
                .body("message", containsString("Periodicidade inválida"));
    }
} 