package br.com.fuctura.indoor.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.exceptions.FoundChildException;
import br.com.fuctura.indoor.exceptions.SituacaoExistsException;
import br.com.fuctura.indoor.repositories.NoticiaRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SituacaoServiceTest {

	@Autowired
	private SituacaoService situacaoService;
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	@BeforeEach	
	public void setUp() throws SituacaoExistsException {
		// situacao 01
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 01");
		if (situacoes.isEmpty()) {
			Situacao situacao = new Situacao("Situacao 01");
			this.situacaoService.insert(situacao);
			// colocar um filho
			Noticia noticia = new Noticia("","",LocalDateTime.now(),LocalDateTime.now(),0);
			noticia.setSituacao(situacao);
			this.noticiaRepository.save(noticia);
		}
		// situacao 02
		situacoes = this.situacaoService.findByDescricao("Situacao 02");
		if (situacoes.isEmpty()) {
			Situacao situacao = new Situacao("Situacao 02");
			this.situacaoService.insert(situacao);			
		}
	}
	
	
	@Test
	@DisplayName("Teste do findById - Sucesso")
	void testFindById_ok() {
		Optional<Situacao> situacao = this.situacaoService.findById(1L);
		assertTrue(situacao.isPresent());
	}
	
	@Test
	@DisplayName("Teste do findById - Falha")
	void testFindById_fail() {
		Optional<Situacao> situacao = this.situacaoService.findById(9L);
		assertTrue(situacao.isEmpty());
	}
	
	@Test
	@DisplayName("Teste do findByDescricao - Sucesso")
	void testFindByDescricao_ok() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 01");
		assertTrue(!situacoes.isEmpty());
	}
	
	@Test
	@DisplayName("Teste do findByDescricao - Falha")
	void testFindByDescricao_fail() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 0");
		assertTrue(situacoes.isEmpty());
	}
	
	@Test
	@DisplayName("Teste do insert - Sucesso")
	void testInsert_ok() {
		Situacao situacao = new Situacao("Situacao 03");
		try {
			this.situacaoService.insert(situacao);
			assertTrue(situacao.getId() > 0);
		} catch (SituacaoExistsException e) {
			assertFalse(true);
		}
	}
	
	@Test
	@DisplayName("Teste do insert - Falha")
	void testInsert_fail() {
		Situacao situacao = new Situacao("Situacao 01");
		try {
			this.situacaoService.insert(situacao);
			assertTrue(false);
		} catch (SituacaoExistsException e) {
			assertFalse(false);
		}
	}
	
	@Test
	@DisplayName("Teste do update - Sucesso")
	void testUpdate_ok() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 01");		
		assertTrue(!situacoes.isEmpty());
		Situacao situacao = situacoes.get(0);
		situacao.setDescricao("Situação 01");
		try {
			this.situacaoService.update(situacao);
			
			assertEquals("Situação 01", situacao.getDescricao());
		} catch (SituacaoExistsException e) {
			assertFalse(true);
		}		
	}
	
	@Test
	@DisplayName("Teste do update - Falha")
	void testUpdate_fail() {
		Optional<Situacao> situacao = this.situacaoService.findById(1L);		
		assertTrue(situacao.isPresent());
		try {
			this.situacaoService.update(situacao.get());
			assertTrue(false);
		} catch (SituacaoExistsException e) {
			assertFalse(false);
		}		
	}
	
	@Test
	@DisplayName("Teste do delete - Sucesso")
	void testDelete_ok() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 02");
		assertTrue(!situacoes.isEmpty());
		try {
			this.situacaoService.delete(situacoes.get(0));
		} catch (FoundChildException e) {
			assertFalse(true);
		}
	}
	
	@Test
	@DisplayName("Teste do delete - Falha")
	void testDelete_fail() {
		Optional<Situacao> situacao = this.situacaoService.findById(1L);
		assertTrue(situacao.isPresent());
		try {
			this.situacaoService.delete(situacao.get());
			assertTrue(false);
		} catch (FoundChildException e) {
			assertFalse(false);
		}
	}
		
	@Test
	@DisplayName("Teste do isExists - Sucesso")
	void testIsExists_ok() {
		boolean situacaoExists = this.situacaoService.isExists(new Situacao("Situacao 02"));
		assertTrue(situacaoExists);
	}
	
	@Test
	@DisplayName("Teste do isExists - Falha")
	void testIsExists_fail() {
		boolean situacaoExists = this.situacaoService.isExists(new Situacao("Situacao 09"));
		assertFalse(situacaoExists);		
	}
}
