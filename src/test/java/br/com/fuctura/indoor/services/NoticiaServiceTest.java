package br.com.fuctura.indoor.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NoticiaServiceTest {
	
	@Autowired 
	private NoticiaService noticiaService;
	
	@Autowired
	private SituacaoService situacaoService;
	
	@BeforeEach
	public void setUp() {
		Situacao situacao = new Situacao("Situacao 1");
		this.situacaoService.insert(situacao);
		if (!this.noticiaService.isExists("Noticia 1")) {
			Noticia noticia = new Noticia("Titulo 1", "Noticia 1",
					LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 20);
			this.noticiaService.insert(noticia);			
		}
	}
	
	@Test
	@DisplayName("Teste o findById - Sucesso")
	public void testFindById_ok() {
		List<Noticia> noticias = this.noticiaService.findByDescricao("Noticia 1");
		assertTrue(!noticias.isEmpty());
	}
	
}
