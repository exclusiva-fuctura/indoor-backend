package br.com.fuctura.indoor.constraints;

public class QueriesConstraints {
	public static final String NOTICIAS_DISPONIVEIS = ""
			+ "SELECT * " 
			+ "FROM NOTICIAS "
			+ "WHERE not_inicio <= :dt_ini "
			+ "AND not_fim >= :dt_fim "
			+ "AND not_sit_id = 2";
	
	public static final String NOTICIAS_DISPONIVEIS_DATA_REF = ""
			+ "SELECT * " 
			+ "FROM NOTICIAS "
			+ "WHERE not_inicio <= :dt_ref "
			+ "AND not_fim >= :dt_ref "
			+ "AND not_sit_id = 2";
}
