package com.example.sbwriterflatfileitemwriterflatcustomizado.Job.writer;

import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.GrupoLancamento;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;

@Component
public class DemonstrativoOrcamentarioRodape implements FlatFileFooterCallback,
        ItemWriteListener<GrupoLancamento> {

    private Double totalGeral = 0.0;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.append("\n");
        writer.append(String.format("\t\t\t\t\t\t\t  Total: %s\n", NumberFormat.getCurrencyInstance().format(totalGeral)));
        writer.append(String.format("\t\t\t\t\t\t\t  Código de Autenticação: %s", "fkyew6868fewjfhjjewf"));
    }

    @Override
    public void beforeWrite(Chunk<? extends GrupoLancamento> chunk) {
        System.out.println("FUI CHAMADO");
        for (GrupoLancamento grupoLancamento : chunk.getItems()) {
            totalGeral += grupoLancamento.getTotal();
        }
    }
}
