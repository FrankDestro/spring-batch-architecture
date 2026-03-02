package com.sysout.migracao_dados_to_db.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SftpDownloadService {

    private final SftpConfig sftpConfig;

    @Value("${sftp.dir.remote.download}")
    private String remoteDownloadDir;

    @Value("${sftp.dir.local.download}")
    private String localDownloadDir;


    public int downloadNewFiles() {
        Path localDir = Path.of(localDownloadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(localDir);
            clearLocalDownloadDirectory(localDir);
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel preparar pasta local de download: " + localDir, e);
        }

        int downloadedFilesCount = 0;

        try (SSHClient sshClient = sftpConfig.setupSshClient();
             SFTPClient sftpClient = sshClient.newSFTPClient()) {

            List<RemoteResourceInfo> files = sftpClient.ls(remoteDownloadDir);
            for (RemoteResourceInfo file : files) {
                if (file.isRegularFile()) {
                    File localFile = localDir.resolve(file.getName()).toFile();
                    String remoteFilePath = remoteDownloadDir + "/" + file.getName();
                    sftpClient.get(remoteFilePath, new FileSystemFile(localFile));
                    downloadedFilesCount++;
                    log.info("Arquivo baixado do SFTP: {}", file.getName());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro na rotina de download SFTP", e);
        }
        return downloadedFilesCount;
    }

    private void clearLocalDownloadDirectory(Path localDir) throws IOException {
        List<Path> filesToDelete = new ArrayList<>();
        try (var paths = Files.list(localDir)) {
            paths.filter(Files::isRegularFile).forEach(filesToDelete::add);
        }
        for (Path filePath : filesToDelete) {
            Files.delete(filePath);
        }
    }
}
