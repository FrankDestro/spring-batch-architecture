package com.sysout.transactionsmultiplesdatasource.jobA.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    private int id;
    private String nome;
    private String email;
    private String dataNascimento;
    private int idade;
}
