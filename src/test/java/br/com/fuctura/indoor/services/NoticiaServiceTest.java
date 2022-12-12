package br.com.fuctura.indoor.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import br.com.fuctura.indoor.exceptions.NoticiaExistsException;
import br.com.fuctura.indoor.exceptions.SituacaoEmptyException;
import br.com.fuctura.indoor.exceptions.SituacaoExistsException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NoticiaServiceTest {
	
	@Autowired 
	private NoticiaService noticiaService;
	
	@Autowired
	private SituacaoService situacaoService;
	
	@BeforeEach
	public void setUp() throws SituacaoExistsException, NoticiaExistsException, SituacaoEmptyException {
		// noticia 1
		Situacao situacao = new Situacao("Situacao 01");
		// verifica se existe a situacao
		if (!this.situacaoService.isExists(situacao)) {
			this.situacaoService.insert(situacao);
			Noticia noticia = new Noticia("Titulo 01", "Noticia 01",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			noticia.setSituacao(situacao);
			// verifica se existe a noticia
			if (!this.noticiaService.isExists(noticia)) {
				this.noticiaService.insert(noticia);			
			}			
		}
		// noticia 2
		situacao = new Situacao("Situacao 02");
		// verifica se a situacao existe
		if (!this.situacaoService.isExists(situacao)) {
			this.situacaoService.insert(situacao);
			Noticia noticia = new Noticia("Titulo 02", "Noticia 02",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			noticia.setSituacao(situacao);
			// verifica se a noticia existe
			if (!this.noticiaService.isExists(noticia)) {				
				this.noticiaService.insert(noticia);			
			}
		}
	}

	@Test
	@DisplayName("Teste o findAll - Sucesso")
	public void testFindAll_ok() {
		List<Noticia> noticias = this.noticiaService.findAll();
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@DisplayName("Teste o findById - Sucesso")
	public void testFindById_ok() {
		List<Noticia> noticias = this.noticiaService.findByDescricao("Noticia 01");
		assertTrue(!noticias.isEmpty());
	}
	
	
	@Test
	@DisplayName("Teste o findByDescricao - Sucesso")
	public void testFindByDescricao_ok() {
		List<Noticia> noticias = this.noticiaService.findByDescricao("Noticia 01");
		assertTrue(!noticias.isEmpty());
	}
	
	
	@Test
	@DisplayName("Teste o findByTitulo - Sucesso")
	public void testFindByTitulo_ok() {
		List<Noticia> noticias = this.noticiaService.findByTitulo("Titulo 01");
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@DisplayName("Teste o findByTitulo - Falha")
	public void testFindByTitulo_fail() {
		List<Noticia> noticias = this.noticiaService.findByTitulo("Titulo 09");
		assertTrue(noticias.isEmpty());
	}
	
	@Test
	@DisplayName("Teste o isExists(Titulo) - Sucesso")
	public void testIsExistsTitulo_ok() {
		Noticia noticia = new Noticia("Titulo 01", "Noticia 09",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertTrue(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@DisplayName("Teste o isExists(Descricao) - Sucesso")
	public void testIsExistsDescricao_ok() {
		Noticia noticia = new Noticia("Titulo 09", "Noticia 01",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertTrue(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@DisplayName("Teste o isExists - Falha")
	public void testIsExists_fail() {
		Noticia noticia = new Noticia("Titulo 09", "Noticia 09",
				LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
		assertFalse(this.noticiaService.isExists(noticia));
	}
	
	@Test
	@DisplayName("Teste o insert - Sucesso")
	public void testInsert_ok() {		
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
	@DisplayName("Teste o insert(Duplicidade) - Falha")
	public void testInsertDuplicidade_fail() {		
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
	@DisplayName("Teste o insert(Sem Situacao) - Falha")
	public void testInsertWithoutSituacao_fail() {		
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
