package br.com.fuctura.indoor.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.fuctura.indoor.entities.Situacao;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SituacaoRepositoryTest {

	@Autowired
	private SituacaoRepository situacaoRepository;
	
	@BeforeEach
	public void setUp() {
		Situacao s1 = new Situacao();
		s1.setDescricao("Situacao 1");
		
		Situacao s2 = new Situacao();
		s2.setDescricao("Situacao 2");
				
		this.situacaoRepository.save(s1);
		this.situacaoRepository.save(s2);	
	}
	
	@AfterEach
	public void setDown() {
		this.situacaoRepository.deleteAll();
	}
	
	@Test
	public void testFindAll_ok() {
		List<Situacao> lista = this.situacaoRepository.findAll();
		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void testFindOne_ok() {
		Situacao situacao = new Situacao();
		situacao.setDescricao("Situacao 1");
	
		Example<Situacao> example = Example.of(situacao);
		Optional<Situacao> retorno = this.situacaoRepository.findOne(example);
		assertTrue(retorno.isPresent());
	}
	
	@Test
	public void testFindByDescricao_ok() {	
		List<Situacao> situacoes = this.situacaoRepository.findByDescricao("Situacao 1");
		assertTrue(!situacoes.isEmpty());
	}
	
	@Test
	public void testFindOne_fail() {
		Situacao situacao = new Situacao();
		situacao.setDescricao("Situacao");
		
		Example<Situacao> example = Example.of(situacao);
		Optional<Situacao> retorno = this.situacaoRepository.findOne(example);
		assertFalse(retorno.isPresent());
	}
}
