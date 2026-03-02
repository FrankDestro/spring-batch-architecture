package com.sysout.sbreaderarquivolargurafixa.Job.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private String nome;
    private String sobrenome;
    private String idade;
    private String email;

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + "'" +
                ", sobrenome ='" + sobrenome + "'" +
                ", idade='" + idade + "'" +
                ", email='" + email + "'" +
                '}';
    }
}
