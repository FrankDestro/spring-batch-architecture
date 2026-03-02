package com.sysout.sb_processador_validacao.job.processor;

import com.sysout.sb_processador_validacao.job.domain.Cliente;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ProcessadorValidacaoProcessorConfig {

    private Set<String> emails = new HashSet<>();

    @Bean
    public ItemProcessor<Cliente, Cliente> procesadorValidacaoProcessor() throws Exception {
        return new CompositeItemProcessorBuilder<Cliente, Cliente>()
                .delegates(beanValidatingProcessor(), emailValidatingProcessor())
                .build();
    }

    private BeanValidatingItemProcessor<Cliente> beanValidatingProcessor() throws Exception {
        BeanValidatingItemProcessor<Cliente> processor = new BeanValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        return processor;
    }

    private ValidatingItemProcessor<Cliente> emailValidatingProcessor (){
        ValidatingItemProcessor<Cliente> processor = new ValidatingItemProcessor<>();
        processor.setValidator(validator());
        processor.setFilter(true);
        return processor;
    }

    private Validator<Cliente> validator() {
        return cliente -> {
            if (emails.contains(cliente.getEmail()))
                throw new ValidationException(String.format("O Cliente %S ja foi processado!", cliente.getEmail()));
            emails.add(cliente.getEmail());
        };
    }
}
