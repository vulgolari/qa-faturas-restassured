package com.example.qa.test;

import com.example.qa.base.BaseTest;
import com.example.qa.model.Contrato;
import com.example.qa.model.Fatura;
import com.example.qa.model.Pagamento;
import com.example.qa.service.ContratoService;
import com.example.qa.service.PagamentoService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de Pagamento")
public class PagamentoTest extends BaseTest {
    
    private final ContratoService contratoService = new ContratoService();
    private final PagamentoService pagamentoService = new PagamentoService();
    private Long contratoId;
    private Long faturaId;
    
    @BeforeEach
    public void setupTest() {
        // Criar contrato e obter fatura para os testes
        Contrato contrato = new Contrato();
        contrato.setClienteId(1L);
        contrato.setDataInicio(LocalDateTime.now().toLocalDate());
        contrato.setDataTermino(LocalDateTime.now().plusYears(1).toLocalDate());
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade("MENSAL");
        
        Response responseContrato = contratoService.criarContrato(contrato);
        contratoId = responseContrato.jsonPath().getLong("id");
        
        Response responseFaturas = contratoService.buscarFaturasPorContrato(contratoId);
        faturaId = responseFaturas.jsonPath().getLong("[0].id");
    }
    
    @Test
    @DisplayName("Deve processar pagamento via boleto com sucesso")
    public void deveProcessarPagamentoViaBoletoComSucesso() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setContratoId(contratoId);
        pagamento.setFaturaId(faturaId);
        pagamento.setValor(100.00);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setMetodoPagamento("BOLETO");
        
        // Act
        Response response = pagamentoService.processarPagamento(pagamento);
        
        // Assert
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("PROCESSADO"));
    }
    
    @Test
    @DisplayName("Deve processar pagamento via cartão com sucesso")
    public void deveProcessarPagamentoViaCartaoComSucesso() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setContratoId(contratoId);
        pagamento.setFaturaId(faturaId);
        pagamento.setValor(100.00);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setMetodoPagamento("CARTAO");
        
        // Act
        Response response = pagamentoService.processarPagamento(pagamento);
        
        // Assert
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("PROCESSADO"));
    }
    
    @Test
    @DisplayName("Não deve processar pagamento com valor maior que o valor da fatura")
    public void naoDeveProcessarPagamentoComValorMaiorQueFatura() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setContratoId(contratoId);
        pagamento.setFaturaId(faturaId);
        pagamento.setValor(1000.00); // Valor maior que o esperado
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setMetodoPagamento("BOLETO");
        
        // Act
        Response response = pagamentoService.processarPagamento(pagamento);
        
        // Assert
        response.then()
                .statusCode(400)
                .body("message", containsString("Valor do pagamento excede o valor da fatura"));
    }
    
    @Test
    @DisplayName("Deve atualizar status da fatura após pagamento")
    public void deveAtualizarStatusFaturaAposPagamento() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setContratoId(contratoId);
        pagamento.setFaturaId(faturaId);
        pagamento.setValor(100.00);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setMetodoPagamento("BOLETO");
        
        // Act
        pagamentoService.processarPagamento(pagamento);
        Response responseFatura = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Assert
        responseFatura.then()
                .statusCode(200)
                .body("find { it.id == " + faturaId + " }.status", equalTo("PAGA"));
    }
} 