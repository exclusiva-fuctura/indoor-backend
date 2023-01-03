package br.com.fuctura.indoor.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.exceptions.NoticiaExistsException;
import br.com.fuctura.indoor.exceptions.SituacaoEmptyException;
import br.com.fuctura.indoor.exceptions.SituacaoExistsException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class NoticiaServiceTest {
	
	@Autowired 
	private NoticiaService noticiaService;
	
	@Autowired
	private SituacaoService situacaoService;
	
	@BeforeEach
	void setUp() throws SituacaoExistsException, NoticiaExistsException, SituacaoEmptyException {
		// Situacao 1
		Situacao situacao = new Situacao("Situacao 1");
		// verifica se existe a situacao
		if (!this.situacaoService.isExists(situacao)) {
			this.situacaoService.insert(situacao);
		} else {
			List<Situacao> sit = this.situacaoService.findByDescricao(situacao.getDescricao());
			situacao = sit.get(0);
		}
		// situacao 2
		Situacao situacao2 = new Situacao("Situacao 2");
		// verifica se a situacao existe
		if (!this.situacaoService.isExists(situacao2)) {
			this.situacaoService.insert(situacao2);
		} else {
			List<Situacao> sit = this.situacaoService.findByDescricao(situacao2.getDescricao());
			situacao2 = sit.get(0);
		}
		
		
		// noticia 1		
		Noticia noticia = new Noticia("Titulo 01", "Noticia 01",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		noticia.setSituacao(situacao);
		// verifica se existe a noticia
		if (!this.noticiaService.isExists(noticia)) {
			this.noticiaService.insert(noticia);			
		}			
	
		// ## noticia 2
		noticia = new Noticia("Titulo 02", "Noticia 02",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		noticia.setSituacao(situacao2);
		// verifica se a noticia existe
		if (!this.noticiaService.isExists(noticia)) {				
			this.noticiaService.insert(noticia);			
		}
	}

	@Test
	@Order(1)
	@DisplayName("Teste o findAll - Sucesso")
	void testFindAll_ok() {
		List<Noticia> noticias = this.noticiaService.findAll();
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@Order(2)
	@DisplayName("Teste o findById - Sucesso")
	void testFindById_ok() {
		Optional<Noticia> noticia = this.noticiaService.findById(2L);
		assertTrue(noticia.isPresent());
	}
		
	@Test
	@Order(3)
	@DisplayName("Teste o findByDescricao - Sucesso")
	void testFindByDescricao_ok() {
		List<Noticia> noticias = this.noticiaService.findByDescricao("Noticia 01");
		assertTrue(!noticias.isEmpty());
	}
	
	
	@Test
	@Order(4)
	@DisplayName("Teste o findByTitulo - Sucesso")
	void testFindByTitulo_ok() {
		List<Noticia> noticias = this.noticiaService.findByTitulo("Titulo 01");
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@Order(5)
	@DisplayName("Teste o findByTitulo - Falha")
	void testFindByTitulo_fail() {
		List<Noticia> noticias = this.noticiaService.findByTitulo("Titulo 09");
		assertTrue(noticias.isEmpty());
	}
	
	@Test
	@Order(6)
	@DisplayName("Teste o isExists(Titulo) - Sucesso")
	void testIsExistsTitulo_ok() {
		Noticia noticia = new Noticia("Titulo 01", "Noticia 09",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertTrue(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@Order(7)
	@DisplayName("Teste o isExists(Descricao) - Sucesso")
	void testIsExistsDescricao_ok() {
		Noticia noticia = new Noticia("Titulo 09", "Noticia 01",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertTrue(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@Order(8)
	@DisplayName("Teste o isExists - Falha")
	void testIsExists_fail() {
		Noticia noticia = new Noticia("Titulo 09", "Noticia 09",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertFalse(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@Order(9)
	@DisplayName("Teste o insert - Sucesso")
	void testInsert_ok() {		
		try {
			Noticia noticia = new Noticia("Titulo 03", "Noticia 03",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			Optional<Situacao> situacao = this.situacaoService.findById(1L);
			assertTrue(situacao.isPresent());
			noticia.setSituacao(situacao.get());
			this.noticiaService.insert(noticia);
			assertNotNull(noticia.getId());
		} catch (NoticiaExistsException | SituacaoEmptyException e) {
			assertFalse(true);
		}
	}
	
	@Test
	@Order(10)
	@DisplayName("Teste o insert(Duplicidade) - Falha")
	void testInsertDuplicidade_fail() {		
		try {
			Noticia noticia = new Noticia("Titulo 01", "Noticia 01",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			this.noticiaService.insert(noticia);
			assertNotNull(noticia.getId());
		} catch (NoticiaExistsException | SituacaoEmptyException e) {
			assertFalse(false);
		}
	}
	
	@Test
	@Order(11)
	@DisplayName("Teste o insert(Sem Situacao) - Falha")
	void testInsertWithoutSituacao_fail() {		
		try {
			Noticia noticia = new Noticia("Titulo 01", "Noticia 01",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			this.noticiaService.insert(noticia);
			assertNotNull(noticia.getId());
		} catch (NoticiaExistsException | SituacaoEmptyException e) {
			assertFalse(false);
		}
	}
	
	@Test
	@Order(12)
	@DisplayName("Teste o insert(Sem Situacao padrão) - Falha")
	void testInsertWithoutDefaultSituacao_fail() {		
		try {
			Noticia noticia = new Noticia("Titulo 01", "Noticia 01",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			this.noticiaService.insert(noticia);
			assertNotNull(noticia.getId());
		} catch (NoticiaExistsException | SituacaoEmptyException e) {
			assertFalse(false);
		}
	}
}
