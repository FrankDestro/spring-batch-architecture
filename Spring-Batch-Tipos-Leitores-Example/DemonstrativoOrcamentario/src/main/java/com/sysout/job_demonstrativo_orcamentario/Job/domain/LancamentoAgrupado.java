package com.sysout.job_demonstrativo_orcamentario.Job.domain;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LancamentoAgrupado {

    private Long codigo;
    private String categoria;
    private List<Lancamento> itens = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;

    public LancamentoAgrupado(Long codigo, String categoria) {
        this.codigo = codigo;
        this.categoria = categoria;
    }

    public void add(Lancamento lancamento) {
        itens.add(lancamento);
        total = total.add(lancamento.getValorLancamento());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        NumberFormat moedaFormatter =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        sb.append("---- Demonstrativo orçamentário ----\n");

        sb.append("[")
                .append(codigo)
                .append("] ")
                .append(categoria)
                .append(" - ")
                .append(moedaFormatter.format(total))
                .append("\n");

        for (Lancamento lancamento : itens) {
            sb.append("\t [")
                    .append(lancamento.getDataLancamento())
                    .append("] ")
                    .append(lancamento.getDescricaoLancamento())
                    .append(" - ")
                    .append(moedaFormatter.format(lancamento.getValorLancamento()))
                    .append("\n");
        }
        return sb.toString();
    }
}
