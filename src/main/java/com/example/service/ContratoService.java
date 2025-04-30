package com.example.service;

import com.example.model.Contrato;
import com.example.model.Fatura;
import com.example.repository.ContratoRepository;
import com.example.repository.FaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContratoService {
    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private FaturaRepository faturaRepository;

    @Transactional
    public Contrato criarContrato(Contrato contrato) {
        Contrato contratoSalvo = contratoRepository.save(contrato);
        gerarFaturas(contratoSalvo);
        return contratoSalvo;
    }

    private void gerarFaturas(Contrato contrato) {
        LocalDate dataInicio = contrato.getDataInicio();
        LocalDate dataTermino = contrato.getDataTermino();
        double valorTotal = contrato.getValorTotal();
        int numeroParcelas = calcularNumeroParcelas(dataInicio, dataTermino, contrato.getPeriodicidade());
        double valorParcela = valorTotal / numeroParcelas;

        for (int i = 0; i < numeroParcelas; i++) {
            Fatura fatura = new Fatura();
            fatura.setContratoId(contrato.getId().toString());
            fatura.setValor(valorParcela);
            fatura.setDataVencimento(calcularDataVencimento(dataInicio, i, contrato.getPeriodicidade()));
            fatura.setStatus(Fatura.Status.PENDENTE);
            faturaRepository.save(fatura);
        }
    }

    private int calcularNumeroParcelas(LocalDate dataInicio, LocalDate dataTermino, Contrato.Periodicidade periodicidade) {
        switch (periodicidade) {
            case MENSAL:
                return (int) java.time.temporal.ChronoUnit.MONTHS.between(dataInicio, dataTermino);
            case BIMESTRAL:
                return (int) java.time.temporal.ChronoUnit.MONTHS.between(dataInicio, dataTermino) / 2;
            case TRIMESTRAL:
                return (int) java.time.temporal.ChronoUnit.MONTHS.between(dataInicio, dataTermino) / 3;
            case SEMESTRAL:
                return (int) java.time.temporal.ChronoUnit.MONTHS.between(dataInicio, dataTermino) / 6;
            case ANUAL:
                return (int) java.time.temporal.ChronoUnit.YEARS.between(dataInicio, dataTermino);
            default:
                throw new IllegalArgumentException("Periodicidade inválida");
        }
    }

    private LocalDate calcularDataVencimento(LocalDate dataInicio, int parcela, Contrato.Periodicidade periodicidade) {
        switch (periodicidade) {
            case MENSAL:
                return dataInicio.plusMonths(parcela);
            case BIMESTRAL:
                return dataInicio.plusMonths(parcela * 2);
            case TRIMESTRAL:
                return dataInicio.plusMonths(parcela * 3);
            case SEMESTRAL:
                return dataInicio.plusMonths(parcela * 6);
            case ANUAL:
                return dataInicio.plusYears(parcela);
            default:
                throw new IllegalArgumentException("Periodicidade inválida");
        }
    }

    public List<Fatura> buscarFaturasPorContrato(String contratoId) {
        return faturaRepository.findByContratoId(contratoId);
    }
} 