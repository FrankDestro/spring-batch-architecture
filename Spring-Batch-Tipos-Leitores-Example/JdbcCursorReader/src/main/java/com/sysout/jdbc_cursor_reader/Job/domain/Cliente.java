package com.sysout.jdbc_cursor_reader.Job.domain;

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
