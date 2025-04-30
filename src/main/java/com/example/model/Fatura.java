package com.example.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "faturas")
public class Fatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contrato_id")
    private String contratoId;

    private Double valor;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double multa;
    private Double juros;

    @ManyToOne
    @JoinColumn(name = "contrato_id", insertable = false, updatable = false)
    private Contrato contrato;

    public enum Status {
        PENDENTE,
        PAGA,
        VENCIDA,
        CANCELADA
    }
} 