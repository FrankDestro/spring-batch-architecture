package com.sysout.sb_processador_classifier.job.processor;

import com.sysout.sb_processador_classifier.job.domain.Transacao;
import org.springframework.batch.item.ItemProcessor;

public class TransacaoProcessor implements ItemProcessor<Transacao, Transacao> {

    @Override
    public Transacao process(Transacao transacao) throws Exception {
        System.out.println(String.format("\nAplicando regras de negocio na transacao %s", transacao.getId()));
        return transacao;
    }
}
