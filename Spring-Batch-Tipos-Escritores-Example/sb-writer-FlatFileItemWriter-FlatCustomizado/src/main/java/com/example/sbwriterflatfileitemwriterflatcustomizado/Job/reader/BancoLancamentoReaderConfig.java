package com.example.sbwriterflatfileitemwriterflatcustomizado.Job.reader;

import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.GrupoLancamento;
import com.example.sbwriterflatfileitemwriterflatcustomizado.Job.domain.Lancamento;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class BancoLancamentoReaderConfig {
	@Bean
	public JdbcCursorItemReader<GrupoLancamento> bancoLancamentoReader(
			@Qualifier("appDS1") DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<GrupoLancamento>()
				.name("bancoLancamentoReader")
				.dataSource(dataSource)
				.sql("select * from lancamento")
				.rowMapper(rowMapper())
				.build();
	}

	private RowMapper<GrupoLancamento> rowMapper() {
		return new RowMapper<GrupoLancamento>() {

			@Override
			public GrupoLancamento mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoLancamento grupo = new GrupoLancamento();
				grupo.setCodigoNaturezaDespesa(rs.getInt("codigoNaturezaDespesa"));
				grupo.setDescricaoNaturezaDespesa(rs.getString("descricaoNaturezaDespesa"));
				grupo.setLancamentoTmp(new Lancamento());
				grupo.getLancamentoTmp().setData(rs.getDate("dataLancamento"));
				grupo.getLancamentoTmp().setDescricao(rs.getString("descricaoLancamento"));
				grupo.getLancamentoTmp().setValor(rs.getDouble("valorLancamento"));
				return grupo;
			}
		};
	}
}
