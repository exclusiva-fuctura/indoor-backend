package br.com.fuctura.indoor.constraints;

public class QueriesConstraints {
	public static final String NOTICIAS_DISPONIVEIS = ""
			+ "SELECT * " 
			+ "FROM NOTICIAS "
			+ "WHERE not_dt_inicio >= :dt_ini "
			+ "AND not_dt_final <= : dt_fim "
			+ "AND not_sit_id = 2";
}
