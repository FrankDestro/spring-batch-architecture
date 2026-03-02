package com.sysout.sb_reader_multiplos_registros_associacao.job.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WiterConfig {

    @Bean
    public ItemWriter writerArquivoMultiplosFormatos() {
        return items -> items.forEach(System.out::println);
    }
}
