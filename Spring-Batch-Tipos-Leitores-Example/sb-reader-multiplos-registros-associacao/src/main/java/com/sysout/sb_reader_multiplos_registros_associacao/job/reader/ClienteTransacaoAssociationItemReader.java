package com.sysout.sb_reader_multiplos_registros_associacao.job.reader;

import com.sysout.sb_reader_multiplos_registros_associacao.job.domain.Cliente;
import com.sysout.sb_reader_multiplos_registros_associacao.job.domain.Transacao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.lang.Nullable;

/**
 * ItemReader customizado responsável por ler arquivos flat
 * contendo registros heterogêneos (Cliente e Transacao)
 * e agrupar múltiplas Transacoes em um único Cliente.
 * <p>
 * Este reader:
 * - Delega a leitura física das linhas para um FlatFileItemReader
 * - Controla o estado da leitura para agrupar registros relacionados
 * - Funciona corretamente com MultiResourceItemReader
 * - Participa do ciclo de vida do Spring Batch (open/update/close)
 */
public class ClienteTransacaoAssociationItemReader
        implements ItemStreamReader<Cliente> {
    /**
     * Mantém o próximo objeto lido do delegate.
     * É usado como "lookahead" (peek) para decidir
     * se a próxima linha pertence ao Cliente atual.
     */
    private Object objAtual;

    /**
     * Reader delegado responsável por ler e mapear
     * cada linha do arquivo (Cliente ou Transacao).
     * <p>
     * Normalmente é um FlatFileItemReader configurado
     * com PatternMatchingCompositeLineMapper.
     */
    private FlatFileItemReader<Object> delegate;

    public ClienteTransacaoAssociationItemReader(FlatFileItemReader<Object> delegate) {
        this.delegate = delegate;
    }

    /**
     * Método principal de leitura.
     * <p>
     * Cada chamada retorna um Cliente completamente montado,
     * contendo todas as suas Transacoes associadas.
     * <p>
     * O método só retorna null quando o arquivo é totalmente consumido.
     */
    @Nullable
    @Override
    public Cliente read() throws Exception {
        // Caso ainda não exista um objeto em memória,
        // lê o próximo registro do reader delegado
        if (objAtual == null) {
            objAtual = delegate.read();
        }

        // Espera-se que o primeiro registro seja um Cliente
        Cliente cliente = (Cliente) objAtual;
        objAtual = null;

        // Se houver um Cliente válido, tenta ler todas as
        // Transacoes associadas a ele
        if (cliente != null) {
            while (peek() instanceof Transacao) {
                cliente.getTransacoes().add((Transacao) objAtual);
            }
        }

        return cliente;
    }

    /**
     * Realiza a leitura antecipada (lookahead) do próximo registro
     * sem perder o estado da leitura.
     * <p>
     * Esse método permite verificar se o próximo item é
     * uma Transacao ou o início de um novo Cliente.
     */
    private Object peek() throws Exception {
        objAtual = delegate.read();
        return objAtual;
    }

    /**
     * Abre o stream de leitura.
     * Delegado diretamente para o FlatFileItemReader,
     * garantindo suporte a restart do Job.
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    /**
     * Atualiza o estado da leitura no ExecutionContext,
     * permitindo que o Job seja reiniciado do ponto correto.
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    /**
     * Fecha o stream de leitura.
     */
    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
