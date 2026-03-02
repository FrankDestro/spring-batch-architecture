//package com.sysout.spring_batch.Config.Jobs;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.util.List;
//
//@Configuration
//public class BatchChunckParImpar {
//
//    @Bean
//    public Job imprimeParImparJob(JobRepository jobRepository, Step imprimeParImparStep) {
//        return new JobBuilder("imprimeParImparJob", jobRepository)
//                .start(imprimeParImparStep)
//                .incrementer(new RunIdIncrementer()) // Teste incrementa um runid e repete o job varias vezes
//                .build();
//    }
//
//    @Bean
//    Step imprimeParImparStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("imprimeParImparStep", jobRepository)
//                .<Integer, String>chunk(10, transactionManager) // Commit Interval
//                .reader(contaAteDezReader())
//                .processor(parOuImparProcessor())
//                .writer(parOuImparWriter())
//                .build();
//    }
//
//    private ItemReader<Integer> contaAteDezReader() {
//        List<Integer> numerosDeUmAteDez = List.of(1,2,3,4,5,6,7,8,9,10);
//        return new ListItemReader<>(numerosDeUmAteDez);
//    }
//
//    private ItemProcessor<? super Integer, String> parOuImparProcessor() {
//        return item -> item % 2 == 0
//                ? String.format("Item %s é Par", item)
//                : String.format("Item %s é Ímpar", item);
//    }
//
//    private ItemWriter<? super String> parOuImparWriter() {
//        return itens -> itens.forEach(System.out::println);
//    }
//}
