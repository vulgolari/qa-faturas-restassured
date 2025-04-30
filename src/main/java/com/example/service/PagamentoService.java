package com.example.service;

import com.example.model.Fatura;
import com.example.model.Pagamento;
import com.example.repository.FaturaRepository;
import com.example.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private FaturaRepository faturaRepository;

    @Transactional
    public Pagamento processarPagamento(String contratoId, String faturaId, double valor, String metodoPagamento) {
        Fatura fatura = faturaRepository.findById(Long.parseLong(faturaId))
                .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));

        if (fatura.getStatus() == Fatura.Status.PAGA) {
            throw new IllegalStateException("Fatura já está paga");
        }

        // Calcula multa e juros se a fatura estiver vencida
        if (fatura.getDataVencimento().isBefore(LocalDateTime.now().toLocalDate())) {
            calcularMultaEJuros(fatura);
        }

        // Cria o registro de pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setContratoId(contratoId);
        pagamento.setFaturaId(faturaId);
        pagamento.setValorPago(valor);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setMetodoPagamento(Pagamento.MetodoPagamento.valueOf(metodoPagamento));

        // Atualiza o status da fatura
        fatura.setStatus(Fatura.Status.PAGA);
        faturaRepository.save(fatura);

        return pagamentoRepository.save(pagamento);
    }

    private void calcularMultaEJuros(Fatura fatura) {
        double valorOriginal = fatura.getValor();
        double multa = valorOriginal * 0.02; // 2% de multa
        double juros = valorOriginal * 0.01; // 1% de juros por dia de atraso

        fatura.setMulta(multa);
        fatura.setJuros(juros);
        fatura.setStatus(Fatura.Status.VENCIDA);
    }

    public List<Pagamento> buscarPagamentosPorFatura(String contratoId, String faturaId) {
        return pagamentoRepository.findByContratoIdAndFaturaId(contratoId, faturaId);
    }
} 