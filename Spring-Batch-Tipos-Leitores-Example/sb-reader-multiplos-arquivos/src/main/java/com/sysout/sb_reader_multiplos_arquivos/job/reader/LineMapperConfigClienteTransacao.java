package com.sysout.sb_reader_multiplos_arquivos.job.reader;

import com.sysout.sb_reader_multiplos_arquivos.job.domain.Cliente;
import com.sysout.sb_reader_multiplos_arquivos.job.domain.Transacao;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do LineMapper responsável por interpretar
 * arquivos flat com múltiplos tipos de registros.
 *
 * Cada linha do arquivo é identificada por um padrão
 * (ex: "0*" ou "1*") e mapeada para um objeto diferente:
 *
 * - Linhas iniciadas com "0" → Cliente
 * - Linhas iniciadas com "1" → Transacao
 *
 * Essa configuração é utilizada pelo FlatFileItemReader
 * para transformar linhas em objetos de domínio.
 */

@Configuration
public class LineMapperConfigClienteTransacao {

    /**
     * LineMapper composto que escolhe dinamicamente
     * o LineTokenizer e o FieldSetMapper com base
     * no padrão da linha lida.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public PatternMatchingCompositeLineMapper lineMapper() {

        // Instancia do PatternMatchingCompositeLineMapper
        PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper<>();

        // Define qual tokenizer será usado para cada padrão de linha
        lineMapper.setTokenizers(tokenizers());

        // Define como cada conjunto de campos será mapeado
        // para o respectivo objeto de domínio
        lineMapper.setFieldSetMappers(fieldSetMappers());

        return lineMapper;
    }

    /**
     * Mapeia padrões de linha para seus respectivos tokenizers.
     *
     * Exemplo:
     * - "0*" → linha de Cliente
     * - "1*" → linha de Transacao
     */
    private Map<String, LineTokenizer> tokenizers() {
        Map<String, LineTokenizer> tokenizerMap = new HashMap<>();
        tokenizerMap.put("0*", clientTokenizer());
        tokenizerMap.put("1*", transactionTokenizer());
        return tokenizerMap;
    }

    /**
     * Tokenizer responsável por quebrar linhas de Cliente.
     *
     * Espera um layout delimitado onde:
     * índice 0 → identificador do tipo de registro (ignorado)
     * índice 1..4 → dados do Cliente
     * example : 0,João,Silva,32,joao@test.com
     */
    private LineTokenizer clientTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        // Nomes dos campos do objeto Cliente
        lineTokenizer.setNames("nome", "sobrenome", "idade", "email");
        // Ignora o primeiro campo (tipo de registro)
        lineTokenizer.setIncludedFields(1, 2, 3, 4);
        return lineTokenizer;
    }

    /**
     * Tokenizer responsável por quebrar linhas de Transacao.
     *
     * Espera um layout delimitado onde:
     * índice 0 → identificador do tipo de registro (ignorado)
     * índice 1..3 → dados da Transacao
     * example : 1,t1c1,Cadeado,50.80
     */
    private LineTokenizer transactionTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        // Nomes dos campos do objeto Transacao
        lineTokenizer.setNames("id", "descricao", "valor");
        // Ignora o primeiro campo (tipo de registro)
        lineTokenizer.setIncludedFields(1, 2, 3);
        return lineTokenizer;
    }

    /**
     * Define como os campos tokenizados serão convertidos
     * em objetos de domínio.
     *
     * Cada padrão de linha aponta para um FieldSetMapper
     * específico.
     */
    @SuppressWarnings({"rawtypes"})
    private Map<String, FieldSetMapper> fieldSetMappers() {
        Map<String, FieldSetMapper> fieldSetMappers = new HashMap<>();
        fieldSetMappers.put("0*", fieldSetMapper(Cliente.class));
        fieldSetMappers.put("1*", fieldSetMapper(Transacao.class));
        return fieldSetMappers;
    }

    /**
     * Cria um FieldSetMapper baseado em BeanWrapper,
     * que mapeia automaticamente os campos do arquivo
     * para as propriedades do objeto de destino.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private FieldSetMapper fieldSetMapper(Class<?> classe) {
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(classe);
        return fieldSetMapper;
    }
}
