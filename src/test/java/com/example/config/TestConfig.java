package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public class TestConfig {
    protected static WireMockServer wireMockServer;
    protected static final int PORT = 8089; // Mudando para uma porta menos comum

    @BeforeAll
    public static void setup() {
        // Inicializa o servidor mock em uma porta específica
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor("localhost", PORT);

        // Configuração base do RestAssured
        RestAssured.baseURI = "http://localhost:" + PORT;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Configuração do ObjectMapper para suporte a LocalDate
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> objectMapper
            )
        );
    }
} 