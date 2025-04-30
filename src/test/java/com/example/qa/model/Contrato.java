package com.example.qa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Contrato {
    private Long id;
    private Long clienteId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTermino;
    
    private Double valorTotal;
    private String periodicidade; // MENSAL, TRIMESTRAL, ANUAL

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }
} 