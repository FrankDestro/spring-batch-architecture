package com.sysout.sb_processador_classifier.job.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    public String id;
    public String descricao;
    public Double valor;

    @Override
    public String toString() {
        return "Transacao{" + "id='" + id + "'" + ", descricao='" + descricao + "'" + ", valor='" + valor + "'" + '}';
    }

}
