package com.sysout.job_demonstrativo_orcamentario.Job.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {
    private Long codigoNaturezaDespesa;
    private String descricaoNaturezaDespesa;
    private String descricaoLancamento;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dataLancamento;
    private BigDecimal valorLancamento;

}
