package com.sysout.sb_processador_classifier.job.processor;

import com.sysout.sb_processador_classifier.job.domain.Cliente;
import org.springframework.batch.item.ItemProcessor;


public class ClienteProcessor implements ItemProcessor<Cliente, Cliente> {

    @Override
    public Cliente process(Cliente cliente) throws Exception {
        System.out.println(String.format("\nAplicando regras de negocio no cliente %s", cliente.getEmail()));
       return cliente;
    }
}
