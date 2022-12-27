package br.com.fuctura.indoor.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

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
class SituacaoRepositoryTest {

	@Autowired
	private SituacaoRepository situacaoRepository;
	
	@BeforeEach
	public void setUp() {
		Situacao s1 = new Situacao();
		s1.setDescricao("Situacao 1");
		
		Situacao s2 = new Situacao();
		s2.setDescricao("Situacao 2");
				
		// verifica se existe a situacao 1
		if (this.situacaoRepository.findByDescricao(s1.getDescricao()).isEmpty()) {
			this.situacaoRepository.save(s1);			
		}
		// verifica se existe a situacao 2
		if (this.situacaoRepository.findByDescricao(s2.getDescricao()).isEmpty()) {
			this.situacaoRepository.save(s2);			
		}
	}
		
	@Test
	void testFindAll_ok() {
		List<Situacao> lista = this.situacaoRepository.findAll();
		assertFalse(lista.isEmpty());
	}
	
	@Test
	void testFindOne_ok() {
		Situacao situacao = new Situacao();
		situacao.setDescricao("Situacao 1");
	
		Example<Situacao> example = Example.of(situacao);
		Optional<Situacao> retorno = this.situacaoRepository.findOne(example);
		assertTrue(retorno.isPresent());
	}
	
	@Test
	void testFindByDescricao_ok() {	
		List<Situacao> situacoes = this.situacaoRepository.findByDescricao("Situacao 1");
		assertTrue(!situacoes.isEmpty());
	}
	
	@Test
	void testFindOne_fail() {
		Situacao situacao = new Situacao();
		situacao.setDescricao("Situacao");
		
		Example<Situacao> example = Example.of(situacao);
		Optional<Situacao> retorno = this.situacaoRepository.findOne(example);
		assertFalse(retorno.isPresent());
	}
}
