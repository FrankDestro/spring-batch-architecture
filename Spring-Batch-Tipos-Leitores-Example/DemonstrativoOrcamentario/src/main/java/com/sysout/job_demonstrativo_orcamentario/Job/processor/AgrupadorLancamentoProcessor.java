package com.sysout.job_demonstrativo_orcamentario.Job.processor;

import com.sysout.job_demonstrativo_orcamentario.Job.domain.Lancamento;
import com.sysout.job_demonstrativo_orcamentario.Job.domain.LancamentoAgrupado;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class AgrupadorLancamentoProcessor implements ItemProcessor<Lancamento, LancamentoAgrupado> {

    private Long codigoAtual;
    private LancamentoAgrupado grupoAtual;

    @Nullable
    @Override
    public LancamentoAgrupado process(@NonNull Lancamento item) throws Exception {

        // Primeira linha
        if (codigoAtual == null) {
            iniciarGrupo(item);
            return null;
        }

        // Mesmo grupo → acumula
        if (item.getCodigoNaturezaDespesa().equals(codigoAtual)) {
            grupoAtual.add(item);
            return null;
        }

        LancamentoAgrupado grupoFechado = grupoAtual;

        iniciarGrupo(item);

        return grupoFechado;
    }

    private void iniciarGrupo(Lancamento item) {
        codigoAtual = item.getCodigoNaturezaDespesa();
        grupoAtual = new LancamentoAgrupado(
                item.getCodigoNaturezaDespesa(),
                item.getDescricaoNaturezaDespesa()
        );
        grupoAtual.add(item);
    }
}

