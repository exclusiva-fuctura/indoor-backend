package br.com.fuctura.indoor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fuctura.indoor.entities.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{

}
