package com.example.qa.test;

import com.example.qa.base.BaseTest;
import com.example.qa.model.Contrato;
import com.example.qa.model.Fatura;
import com.example.qa.service.ContratoService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de Vencimento de Faturas")
public class VencimentoTest extends BaseTest {
    
    private final ContratoService contratoService = new ContratoService();
    private Long contratoId;
    private Long faturaId;
    
    @BeforeEach
    public void setupTest() {
        // Criar contrato e obter fatura para os testes
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDate.now().minusMonths(1));
        contrato.setDataTermino(LocalDate.now().plusMonths(11));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        Response responseContrato = contratoService.criarContrato(contrato);
        contratoId = responseContrato.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        faturaId = responseFaturas.jsonPath().getLong("[0].id");
    }
    
    @Test
    @DisplayName("Deve marcar fatura como vencida após data de vencimento")
    public void deveMarcarFaturaComoVencidaAposVencimento() {
        // Arrange
        Response responseFatura = contratoService.buscarFaturasPorContrato(contratoId);
        LocalDate dataVencimento = LocalDate.parse(responseFatura.jsonPath().getString("[0].dataVencimento"));
        
        // Simular passagem do tempo (em um ambiente real, isso seria feito através de um job)
        // Act
        Response responseAtualizada = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseAtualizada.then()
                .statusCode(200)
                .body("find { it.id == " + faturaId + " }.status", equalTo("VENCIDA"));
    }
    
    @Test
    @DisplayName("Deve aplicar multa e juros em fatura vencida")
    public void deveAplicarMultaEJurosEmFaturaVencida() {
        // Arrange
        Response responseFatura = contratoService.buscarFaturasPorContrato(contratoId);
        Double valorOriginal = responseFatura.jsonPath().getDouble("[0].valor");
        
        // Simular passagem do tempo (em um ambiente real, isso seria feito através de um job)
        // Act
        Response responseAtualizada = contratoService.buscarFaturasPorContrato(contratoId);
        Double valorComMulta = responseAtualizada.jsonPath().getDouble("find { it.id == " + faturaId + " }.valor");
        
        // Assert
        responseAtualizada.then()
                .statusCode(200)
                .body("find { it.id == " + faturaId + " }.status", equalTo("VENCIDA"));
        
        assertThat("Valor com multa deve ser maior que o valor original", 
                valorComMulta, greaterThan(valorOriginal));
    }
    
    @Test
    @DisplayName("Não deve gerar novas faturas após cancelamento do contrato")
    public void naoDeveGerarNovasFaturasAposCancelamento() {
        // Arrange
        Response responseCancelamento = contratoService.cancelarContrato(contratoId);
        
        // Act
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFaturas.then()
                .statusCode(200)
                .body("size()", equalTo(1)); // Apenas a fatura já gerada deve existir
    }
} 