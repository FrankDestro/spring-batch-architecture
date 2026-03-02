package com.sysout.job_demonstrativo_orcamentario.Job;

import com.sysout.job_demonstrativo_orcamentario.Job.domain.Lancamento;
import com.sysout.job_demonstrativo_orcamentario.Job.domain.LancamentoAgrupado;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class DemonstrativoOrcamentarioJobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job DemonstrativoOrcamentarioJob (Step gerarDemonstrativoOrcamentarioStep) {
        return new JobBuilder("DemonstrativoOrcamentarioJob", jobRepository)
                .start(gerarDemonstrativoOrcamentarioStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public Step gerarDemonstrativoOrcamentarioStep (
            @Qualifier("multiplosArquivosLancamentos") MultiResourceItemReader multiplosArquivosLancamentos,
            ItemProcessor<Lancamento, LancamentoAgrupado> processor,
            @Qualifier("writerDemonstrativoOrcamentario") ItemWriter writerDemonstrativoOrcamentario
    ) {
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

        return new StepBuilder("gerarDemonstrativoOrcamentarioStep", jobRepository)
                .<Lancamento, LancamentoAgrupado> chunk(1, transactionManager)
                .reader(multiplosArquivosLancamentos)
                .processor(processor)
                .writer(writerDemonstrativoOrcamentario)
                .build();
    }
}
