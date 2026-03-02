package com.sysout.sb_reader_multiplos_formatos.job.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private String nome;
    private String sobrenome;
    private String idade;
    private String email;
    private List<Transacao> transacoes = new ArrayList<>();

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + "'" +
                ", sobrenome ='" + sobrenome + "'" +
                ", idade='" + idade + "'" +
                ", email='" + email + "'" +
                (transacoes.isEmpty() ? "" : ", transações " + transacoes) +
                '}';
    }
}
