package com.sysout.sb_processador_classifier.job.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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
