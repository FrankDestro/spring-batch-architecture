package com.sysout.job_demonstrativo_orcamentario.Job.reader;

import com.sysout.job_demonstrativo_orcamentario.Job.domain.Lancamento;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LineMapperConfigDemonstrativoOrcamentario {

    @Bean
    public LineMapper<Lancamento> lancamentoLineMapper() {
        DefaultLineMapper<Lancamento> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("codigoNaturezaDespesa", "descricaoNaturezaDespesa", "descricaoLancamento", "dataLancamento", "valorLancamento");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<Lancamento> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Lancamento.class);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
