package com.sysout.sb_jdbc_paging.Job.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cliente {

    private String nome;
    private String sobrenome;
    private String idade;
    private String email;
}
