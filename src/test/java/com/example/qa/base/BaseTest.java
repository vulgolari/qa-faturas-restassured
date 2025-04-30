package com.example.qa.base;

import com.example.qa.config.WireMockConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

@ExtendWith(WireMockConfig.class)
public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setup() {
        logger.info("Configurando ambiente de testes");
        
        try {
            // Carrega as propriedades do arquivo rest-assured.properties
            Properties props = new Properties();
            props.load(BaseTest.class.getClassLoader().getResourceAsStream("rest-assured.properties"));
            
            // Configura o RestAssured com as propriedades
            RestAssured.baseURI = props.getProperty("baseUri");
            RestAssured.basePath = props.getProperty("basePath");
            RestAssured.port = Integer.parseInt(props.getProperty("port"));
            
            // Habilita logs para falhas de validação
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            
            requestSpec = new RequestSpecBuilder()
                    .setContentType("application/json")
                    .build();

            responseSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .build();
                    
            logger.info("Ambiente de testes configurado com sucesso");
            logger.info("Base URI: " + RestAssured.baseURI);
            logger.info("Base Path: " + RestAssured.basePath);
            logger.info("Port: " + RestAssured.port);
            
        } catch (IOException e) {
            logger.error("Erro ao carregar arquivo de propriedades", e);
            throw new RuntimeException("Erro ao carregar arquivo de propriedades", e);
        }
    }
} 