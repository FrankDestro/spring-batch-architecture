package com.example.sbwriterflatfileitemwriterflatcustomizado.Job.writer;

import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.GrupoLancamento;
import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.Lancamento;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.WritableResource;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class WriterDemonstrativoOrcamentarioConfig {

    @Bean
    @StepScope
    public MultiResourceItemWriter<GrupoLancamento> multiDemonstrativoOrcamentarioWriter(
            @Value("#{jobParameters['demonstrativosOrcamentarios']}") WritableResource demonstrativosOrcamentarios,
            FlatFileItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter
    ) {
        return new MultiResourceItemWriterBuilder<GrupoLancamento>()
                .name("multiDemonstrativoOrcamentarioWriter")
                .resource(demonstrativosOrcamentarios)
                .delegate(demonstrativoOrcamentarioWriter)
                .resourceSuffixCreator(suffixCreator())
                .itemCountLimitPerResource(1)
                .build();
    }

    private ResourceSuffixCreator suffixCreator() {
        return index -> index + ".txt";
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<GrupoLancamento> writerDemonstrativoOrcamentario(
            @Value("#{jobParameters['demonstrativoOrcamentario']}") WritableResource demonstrativoOrcamentario,
            DemonstrativoOrcamentarioRodape rodapeCallBack
    ) {
        return new FlatFileItemWriterBuilder<GrupoLancamento>()
                .name("writerDemonstrativoOrcamentario")
                .resource(demonstrativoOrcamentario)
                .lineAggregator(lineAggregator())
                .headerCallback(cabecalhoCallBack())
                .footerCallback(rodapeCallBack)
                .build();
    }

    private FlatFileHeaderCallback cabecalhoCallBack() {
        return writer -> {
            writer.append(String.format("SISTEMA INTEGRADO: XPTO \t\t\t\t DATA: %s\n", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
            writer.append(String.format("MÓDULO: ORÇAMENTO \t\t\t\t\t\t HORA: %s\n", new SimpleDateFormat("HH:MM").format(new Date())));
            writer.append(String.format("\t\t\tDEMONSTRATIVO ORCAMENTARIO\n"));
            writer.append(String.format("----------------------------------------------------------------------------\n"));
            writer.append(String.format("CODIGO NOME VALOR\n"));
            writer.append(String.format("\t Data Descricao Valor\n"));
            writer.append(String.format("----------------------------------------------------------------------------\n"));
        };
    }

    private LineAggregator<GrupoLancamento> lineAggregator() {
        return grupo -> {
            String formatGrupoLancamento = String.format("[%d] %s - %s\n",
                    grupo.getCodigoNaturezaDespesa(),
                    grupo.getDescricaoNaturezaDespesa(),
                    NumberFormat.getCurrencyInstance().format(grupo.getTotal()));

            StringBuilder strBuilder = new StringBuilder();
            for (Lancamento lancamento : grupo.getLancamentos()) {
                strBuilder.append(String.format("\t [%s] %s - %s\n", new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getData()), lancamento.getDescricao(),
                        NumberFormat.getCurrencyInstance().format(lancamento.getValor())));
            }
            String formatLancamento = strBuilder.toString();
            return formatGrupoLancamento + formatLancamento;
        };
    }
}
