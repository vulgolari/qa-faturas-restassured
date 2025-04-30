package com.example.qa.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WireMockConfig implements BeforeAllCallback, AfterAllCallback {
    private static WireMockServer wireMockServer;

    @Override
    public void beforeAll(ExtensionContext context) {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.options()
                .port(8080)
                .usingFilesUnderDirectory("src/test/resources/wiremock")
        );
        wireMockServer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public static WireMockServer getWireMockServer() {
        return wireMockServer;
    }
} 