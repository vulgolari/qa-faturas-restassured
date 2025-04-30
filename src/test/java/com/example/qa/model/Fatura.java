package com.example.qa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Fatura {
    private Long id;
    private Long contratoId;
    private Double valor;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;
    
    private String status; // PENDENTE, PAGA, VENCIDA

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContratoId() {
        return contratoId;
    }

    public void setContratoId(Long contratoId) {
        this.contratoId = contratoId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 