package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class Pagamento {

    @JsonProperty("contrato_id")
    private String contratoId;

    @JsonProperty("fatura_id")
    private String faturaId;

    @JsonProperty("valor_pago")
    private double valorPago;

    @JsonProperty("data_pagamento")
    private LocalDate dataPagamento;

    @JsonProperty("metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    public enum MetodoPagamento {
        BOLETO,
        CARTAO,
        TRANSFERENCIA
    }

    // Getters e Setters
    public String getContratoId() {
        return contratoId;
    }

    public void setContratoId(String contratoId) {
        this.contratoId = contratoId;
    }

    public String getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(String faturaId) {
        this.faturaId = faturaId;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}