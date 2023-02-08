package br.com.fuctura.indoor.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

import br.com.fuctura.indoor.aux.GenerateData;
import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class NoticiaRepositoryTest {

	@Autowired
	private NoticiaRepository noticiaRepository;
	
	@Autowired
	private SituacaoRepository situacaoRepository;
	
	@BeforeEach
	public void setUp() {
		// obter a situacao
		Situacao situacao = this.createSituacao("Situacao 1");
		// criar a noticia
		this.createNoticia("Noticia 1", situacao);
	}
		
	@Test
	@Order(1)
    @DisplayName("Testar o findBySituação - Sucesso")
	void testFindBySituacao_ok() {
		Optional<Situacao> situacao = this.situacaoRepository.findById(4L);
		if (situacao.isPresent()) {
			List<Noticia> lista = this.noticiaRepository.findBySituacao(situacao.get());
			
			assertFalse(lista.isEmpty());
		} else {
			assertTrue(false);
		}
	}
	
	@Test
	@Order(2)
    @DisplayName("Testar o findBySituação - Falha")	
	void testFindBySituacao_fail() {
		Optional<Situacao> situacao = this.situacaoRepository.findById(4L);
		if (situacao.isPresent()) {
			// alterar o id da situacao
			situacao.get().setId(5L);

			List<Noticia> lista = this.noticiaRepository.findBySituacao(situacao.get());
			
			assertTrue(lista.isEmpty());
		} else {
			assertFalse(false);
		}
	}
	
	@Test
	@Order(3)
    @DisplayName("Testar a findSituacaoId - Sucesso")
	void testFindSituacaoId_ok() {
		List<Noticia> noticias = this.noticiaRepository.findSituacaoId(4L);
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@Order(4)
    @DisplayName("Testar a findSituacaoId - Falha")
	void testFindSituacaoId_fail() {
		List<Noticia> noticias = this.noticiaRepository.findSituacaoId(5L);
		assertTrue(noticias.isEmpty());
	}
	
	@Test
	@Order(5)
    @DisplayName("Testar a findDisponiveis com data inicial e final - Sucesso")
	void testFindDisponiveisWithDataInicialAndDataFinal_ok() {
		// criar noticia 2
		this.createNoticia("Noticia 2", this.createSituacao("Situacao 2"));
		
		LocalDateTime dataInicial = LocalDateTime.now();
		LocalDateTime dataFinal = LocalDateTime.now().plusMinutes(50);

		List<Noticia> noticias = this.noticiaRepository
				.findDisponiveis(dataInicial, dataFinal);
		
		assertTrue(!noticias.isEmpty());
	}

	@Test
	@Order(6)
    @DisplayName("Testar a findDisponiveis com data referencia - Sucesso")
	void testFindDisponiveisWithDataReferencia_ok() {
		LocalDateTime dataRef = LocalDateTime.now();

		List<Noticia> noticias = this.noticiaRepository
				.findDisponiveis(dataRef);
		
		assertTrue(!noticias.isEmpty());
	}
	
	@Test
	@Order(7)
    @DisplayName("Testar a findDisponiveis com data referencia - Falha")
	void testFindDisponiveisWithDataReferencia_fail() {
		// criar data passada
		LocalDateTime dataRef = LocalDateTime.now().minusDays(1);

		List<Noticia> noticias = this.noticiaRepository
				.findDisponiveis(dataRef);
		
		assertTrue(noticias.isEmpty());
	}

	@Test
	@Order(8)
    @DisplayName("Testar a findDisponiveis - Falha")
	void testFindDisponiveis_fail() {
		LocalDateTime dataInicial = LocalDateTime.now();
		LocalDateTime dataFinal = LocalDateTime.now().plusMinutes(60);

		List<Noticia> noticias = this.noticiaRepository
				.findDisponiveis(dataInicial, dataFinal);
		
		assertTrue(noticias.isEmpty());
	}
	
	@Test
	@Order(9)
    @DisplayName("Testar o findAll - Sucesso")
	void testFindAll_ok() {
		List<Noticia> noticias = this.noticiaRepository.findAll();
		assertTrue(!noticias.isEmpty());		
	}
	
	@Test
	@Order(10)
    @DisplayName("Testar o Delete - Sucesso")
	void testDelete_ok() {
		Optional<Noticia> noticia = this.noticiaRepository.findById(4L);
		if (noticia.isPresent()) {
			this.noticiaRepository.delete(noticia.get());
			
			Optional<Noticia> noticiaFound = this.noticiaRepository.findById(4L);
			assertTrue(noticiaFound.isEmpty());
		} else {
			assertTrue(false);
		}
	}
	
	@Test
	@Order(11)
    @DisplayName("Testar a Atualizacao - Sucesso")
	void testUpdate_ok() {
		Optional<Noticia> noticia = this.noticiaRepository.findById(5L);
		if (noticia.isPresent()) {
			String descricaoOriginal = noticia.get().getDescricao();
			// alterar a noticia
			Noticia noticiaChanged = noticia.get();
			noticiaChanged.setDescricao("Noticia 3");
			this.noticiaRepository.save(noticiaChanged);
						
			assertNotEquals(descricaoOriginal, noticiaChanged.getDescricao());
		} else {
			assertTrue(false);
		}
	}
	
	
	private Situacao createSituacao(String descricao) {
		return GenerateData.createSituacao(descricao, this.situacaoRepository);
	}
	
	private void createNoticia(String descricao, Situacao situacao) {
		GenerateData.createNoticia(descricao, situacao, this.noticiaRepository); 
	}
}
