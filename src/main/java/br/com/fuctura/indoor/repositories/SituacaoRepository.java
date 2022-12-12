package br.com.fuctura.indoor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fuctura.indoor.entities.Situacao;

@Repository
public interface SituacaoRepository extends JpaRepository<Situacao, Long> {

	@Query( value = "SELECT count(1) FROM NOTICIAS n WHERE n.not_sit_id = :id", 
			nativeQuery = true)
	long noticiasWithSituacao(@Param("id") long id);

	List<Situacao> findByDescricao(String string);
	

}
