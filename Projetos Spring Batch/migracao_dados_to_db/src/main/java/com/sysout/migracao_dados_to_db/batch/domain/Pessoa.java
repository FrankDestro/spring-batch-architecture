package com.sysout.migracao_dados_to_db.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pessoa {
    private Integer id;
    private String nome;
    private String email;
    private Date dataNascimento;
    private int idade;
}
