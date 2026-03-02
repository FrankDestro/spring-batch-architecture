package com.sysout.sb_reader_multiplos_arquivos.job.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WiterConfig {

    @Bean
    @StepScope
    public ItemWriter writerArquivoMultiplosFormatos() {
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
