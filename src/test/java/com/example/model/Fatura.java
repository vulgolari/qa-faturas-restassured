package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class Fatura {
    private String id;

    @JsonProperty("contrato_id")
    private String contratoId;

    private double valor;

    @JsonProperty("data_vencimento")
    private LocalDate dataVencimento;

    private Status status;
    private double multa;
    private double juros;

    public enum Status {
        PENDENTE,
        PAGA,
        VENCIDA
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContratoId() {
        return contratoId;
    }

    public void setContratoId(String contratoId) {
        this.contratoId = contratoId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }
}
