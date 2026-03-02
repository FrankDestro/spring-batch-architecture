package com.sysout.sb_processador_classifier.job.processor;

import com.sysout.sb_processador_classifier.job.domain.Cliente;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessadorClassifierProcessorConfig {

    @Bean
    public ItemProcessor processadorClassifierProcessor() {
        return new ClassifierCompositeItemProcessorBuilder<>()
                .classifier(classifier())
                .build();
        }

    private Classifier classifier() {
        return (Classifier<Object, ItemProcessor>) object -> {
            if(object instanceof Cliente)
                return new ClienteProcessor();
            else
                return new TransacaoProcessor();
        };
    }
}
