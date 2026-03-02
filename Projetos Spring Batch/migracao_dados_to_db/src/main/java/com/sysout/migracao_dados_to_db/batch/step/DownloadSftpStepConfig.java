package com.sysout.migracao_dados_to_db.batch.step;

import com.sysout.migracao_dados_to_db.integration.SftpDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DownloadSftpStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SftpDownloadService sftpDownloadService;

    @Bean
    public Step downloadSftpStep() {
        return new StepBuilder("downloadSftpStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int count = sftpDownloadService.downloadNewFiles();
                    log.info("Arquivos baixados do SFTP: {}", count);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
