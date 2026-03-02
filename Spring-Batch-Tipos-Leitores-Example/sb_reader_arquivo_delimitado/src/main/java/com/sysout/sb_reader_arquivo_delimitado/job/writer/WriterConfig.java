package com.sysout.sb_reader_arquivo_delimitado.job.writer;

import com.sysout.sb_reader_arquivo_delimitado.job.domain.Cliente;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterConfig {

    @Bean
    @StepScope
    public ItemWriter<Cliente> writerArquivoDelimitation() {
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
