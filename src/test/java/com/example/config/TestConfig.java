package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;

public class TestConfig {
    @BeforeAll
    public static void setup() {
        // Configuração base do RestAssured
        RestAssured.baseURI = "http://localhost:8080";
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