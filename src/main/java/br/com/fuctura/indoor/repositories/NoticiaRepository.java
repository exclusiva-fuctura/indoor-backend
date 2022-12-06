package br.com.fuctura.indoor.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fuctura.indoor.constraints.QueriesConstraints;
import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{

	@Query( value = "SELECT * FROM NOTICIAS n WHERE n.not_sit_id = :situacao", nativeQuery = true)
	public List<Noticia> findSituacaoId(@Param("situacao") long situacao);
	
	@Query( value = QueriesConstraints.NOTICIAS_DISPONIVEIS, nativeQuery = true)
	public List<Noticia> findDisponiveis(@Param("dt_ini") LocalDateTime dt_ini,			
	                                     @Param("dt_fim") LocalDateTime dt_fim);
	
	@Query( value = QueriesConstraints.NOTICIAS_DISPONIVEIS_DATA_REF, nativeQuery = true)
	public List<Noticia> findDisponiveis(@Param("dt_ref") LocalDateTime dt_ref);
	
	public List<Noticia> findBySituacao(Situacao situacao);
		
	public List<Noticia> findByInicioGreaterThanEqualAndFimLessThanEqual(LocalDateTime inicio,
	LocalDateTime fim);
}
