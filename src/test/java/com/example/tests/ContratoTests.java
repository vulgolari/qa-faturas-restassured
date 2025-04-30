package com.example.tests;

import com.example.config.TestConfig;
import com.example.model.Contrato;
import com.example.model.Fatura;
import com.example.model.Pagamento;
import com.example.service.ContratoService;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class ContratoTests extends TestConfig {
    private ContratoService contratoService;
    private Contrato contrato;

    @BeforeEach
    public void init() {
        // Limpa todos os mocks anteriores
        WireMock.reset();
        
        contratoService = new ContratoService();
        contrato = new Contrato();
        contrato.setClienteId("12345");
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataTermino(LocalDate.now().plusYears(1));
        contrato.setValorTotal(1200.00);
        contrato.setPeriodicidade(Contrato.Periodicidade.MENSAL);
    }

    @AfterEach
    public void tearDown() {
        WireMock.reset();
    }

    @Test
    @DisplayName("Deve criar um contrato válido e retornar ID")
    public void deveCriarContratoValido() {
        // Configura o mock
        stubFor(post(urlEqualTo("/contratos"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"contrato_id\": \"123\"}")));

        var response = contratoService.criarContrato(contrato);
        
        // Valida status code
        assertEquals(201, response.getStatusCode(), "Status code deve ser 201 (Created)");
        
        // Valida retorno do ID do contrato
        String contratoId = response.jsonPath().getString("contrato_id");
        assertNotNull(contratoId, "ID do contrato não deve ser nulo");
        assertFalse(contratoId.isEmpty(), "ID do contrato não deve estar vazio");
    }

    @Test
    @DisplayName("Deve gerar faturas automaticamente após criação do contrato")
    public void deveGerarFaturasAutomaticamente() {
        // Configura o mock para criação do contrato
        stubFor(post(urlEqualTo("/contratos"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"contrato_id\": \"123\"}")));

        // Configura o mock para busca de faturas
        stubFor(get(urlEqualTo("/contratos/123/faturas"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[" +
                    "{\"id\":\"1\",\"contrato_id\":\"123\",\"valor\":100.00,\"data_vencimento\":\"2024-05-01\",\"status\":\"PENDENTE\"}," +
                    "{\"id\":\"2\",\"contrato_id\":\"123\",\"valor\":100.00,\"data_vencimento\":\"2024-06-01\",\"status\":\"PENDENTE\"}" +
                    "]")));

        var response = contratoService.criarContrato(contrato);
        String contratoId = response.jsonPath().getString("contrato_id");
        
        List<Fatura> faturas = contratoService.buscarFaturasPorContrato(contratoId);
        
        // Valida se a lista de faturas não está vazia
        assertFalse(faturas.isEmpty(), "Lista de faturas não deve estar vazia");
        
        // Valida estrutura das faturas
        faturas.forEach(fatura -> {
            assertNotNull(fatura.getId(), "ID da fatura não deve ser nulo");
            assertNotNull(fatura.getContratoId(), "ID do contrato na fatura não deve ser nulo");
            assertTrue(fatura.getValor() > 0, "Valor da fatura deve ser maior que zero");
            assertNotNull(fatura.getDataVencimento(), "Data de vencimento não deve ser nula");
            assertNotNull(fatura.getStatus(), "Status da fatura não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve processar pagamento de fatura e atualizar status")
    public void deveProcessarPagamento() {
        // Configura o mock para criação do contrato
        stubFor(post(urlEqualTo("/contratos"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"contrato_id\": \"123\"}")));

        // Configura o mock para busca de faturas
        stubFor(get(urlEqualTo("/contratos/123/faturas"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[" +
                    "{\"id\":\"1\",\"contrato_id\":\"123\",\"valor\":100.00,\"data_vencimento\":\"2024-05-01\",\"status\":\"PENDENTE\"}" +
                    "]")));

        // Configura o mock para processamento de pagamento
        stubFor(post(urlEqualTo("/contratos/123/faturas/1/pagamentos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"status\": \"PAGA\"}")));

        var response = contratoService.criarContrato(contrato);
        String contratoId = response.jsonPath().getString("contrato_id");
        
        List<Fatura> faturas = contratoService.buscarFaturasPorContrato(contratoId);
        String faturaId = faturas.get(0).getId();
        double valorFatura = faturas.get(0).getValor();
        
        var pagamentoResponse = contratoService.processarPagamento(
            contratoId, 
            faturaId, 
            valorFatura, 
            Pagamento.MetodoPagamento.CARTAO.toString()
        );
        
        // Valida status code do pagamento
        assertEquals(200, pagamentoResponse.getStatusCode(), "Status code do pagamento deve ser 200");
        
        // Valida atualização do status da fatura
        assertEquals("PAGA", pagamentoResponse.jsonPath().getString("status"), "Status da fatura deve ser PAGA após pagamento");
    }

    @Test
    @DisplayName("Deve aplicar multa e juros em fatura vencida")
    public void deveAplicarMultaEJuros() {
        // Configura o mock para criação do contrato
        stubFor(post(urlEqualTo("/contratos"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"contrato_id\": \"123\"}")));

        // Configura o mock para busca de faturas
        stubFor(get(urlEqualTo("/contratos/123/faturas"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[" +
                    "{\"id\":\"1\",\"contrato_id\":\"123\",\"valor\":100.00,\"data_vencimento\":\"2024-04-01\",\"status\":\"VENCIDA\",\"multa\":10.00,\"juros\":5.00}" +
                    "]")));

        var response = contratoService.criarContrato(contrato);
        String contratoId = response.jsonPath().getString("contrato_id");
        
        List<Fatura> faturas = contratoService.buscarFaturasPorContrato(contratoId);
        Fatura fatura = faturas.get(0);
        
        // Valida aplicação de multa e juros
        assertTrue(fatura.getMulta() > 0, "Multa deve ser maior que zero para fatura vencida");
        assertTrue(fatura.getJuros() > 0, "Juros devem ser maiores que zero para fatura vencida");
        
        // Valida valor total com multa e juros
        double valorTotal = fatura.getValor() + fatura.getMulta() + fatura.getJuros();
        assertTrue(valorTotal > fatura.getValor(), "Valor total com multa e juros deve ser maior que o valor original");
    }
}
