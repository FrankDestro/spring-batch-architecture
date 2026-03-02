package com.sysout.migracao_dados_to_db.integration;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SftpConfig {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port:22}")
    private int port;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.password}")
    private String password;

    public SSHClient setupSshClient() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(host, port);
        client.setConnectTimeout(10_000);
        client.setTimeout(60_000);
        client.authPassword(user, password);
        return client;
    }


    // CONFIGURAÇÃO PARA AMBIENTE DE PRODUÇÃO.
//    @Configuration
//    public class SftpConfig {
//
//        @Value("${sftp.host}")
//        private String host;
//
//        @Value("${sftp.port:22}")
//        private int port;
//
//        @Value("${sftp.user}")
//        private String user;
//
//        @Value("${sftp.password}")
//        private String password;
//
//        @Value("${sftp.knownhosts.file}")
//        private String knownHostsFile; // caminho para arquivo de chaves conhecidas
//
//        @Value("${sftp.connect.timeout:10000}")
//        private int connectTimeout; // 10s padrão
//
//        @Value("${sftp.operation.timeout:60000}")
//        private int operationTimeout; // 60s padrão
//
//        /**
//         * Configura e conecta um SSHClient seguro ao servidor SFTP.
//         * @return SSHClient pronto para uso
//         * @throws IOException se houver falha na conexão ou verificação
//         */
//        public SSHClient setupSshClient() throws IOException {
//            SSHClient client = new SSHClient();
//
//            // Verificação segura do host via known_hosts
//            File knownHosts = new File(knownHostsFile);
//            if (!knownHosts.exists()) {
//                throw new IOException("Arquivo known_hosts não encontrado: " + knownHostsFile);
//            }
//            client.addHostKeyVerifier(new OpenSSHKnownHosts(knownHosts));
//
//            // Configura timeout
//            client.setConnectTimeout(connectTimeout);
//            client.setTimeout(operationTimeout);
//
//            // Conecta e autentica
//            client.connect(host, port);
//            client.authPassword(user, password);
//
//            return client; // quem chamar deve fechar o client
//        }
//    }
}
