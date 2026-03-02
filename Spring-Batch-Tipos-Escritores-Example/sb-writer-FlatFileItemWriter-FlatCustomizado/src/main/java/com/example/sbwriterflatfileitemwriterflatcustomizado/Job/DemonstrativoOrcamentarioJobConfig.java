package com.example.sbwriterflatfileitemwriterflatcustomizado.Job;

import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.GrupoLancamento;
import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.writer.DemonstrativoOrcamentarioRodape;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
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
            MultiResourceItemReader multiplosArquivosLancamentos,
            MultiResourceItemWriter<GrupoLancamento> writerDemonstrativoOrcamentario,
            DemonstrativoOrcamentarioRodape rodapeCallBack
    ) {
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

        return new StepBuilder("gerarDemonstrativoOrcamentarioStep", jobRepository)
                .<GrupoLancamento,GrupoLancamento> chunk(100, transactionManager)
                .reader(multiplosArquivosLancamentos)
                .writer(writerDemonstrativoOrcamentario)
                .listener(rodapeCallBack)
                .build();
    }
}
