package com.sysout.migracao_dados_to_db.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DadosBancarios {
    private Integer id;
    private Integer pessoaId;
    private Integer agencia;
    private Integer conta;
    private Integer banco;
}
