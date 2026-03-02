package com.sysout.sbreaderarquivolargurafixa.Job.writer;

import com.sysout.sbreaderarquivolargurafixa.Job.Domain.Client;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterConfigArquivoLarguraFixa {

    @Bean
    @StepScope
    public ItemWriter<Client> writerArquivoLarguraFixa() {
        return items -> items.forEach(System.out::println);
//        return items -> {
//            for (Cliente cliente : items) {
//                if (cliente.getNome().equals("Maria")) {
//                    throw new RuntimeException();
//                } else {
//                    System.out.printf(String.valueOf(cliente));
//                }
//            }
//        };
    }
}
