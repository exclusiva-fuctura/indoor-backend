package br.com.fuctura.indoor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fuctura.indoor.entities.Situacao;

@Repository
public interface SituacaoRepository extends JpaRepository<Situacao, Long> {

	List<Situacao> findByDescricao(String string);

}
