package br.com.fuctura.indoor.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

import br.com.fuctura.indoor.entities.Situacao;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SituacaoServiceTest {

	@Autowired
	private SituacaoService situacaoService;
	
	@BeforeEach	
	public void setUp() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 01");
		if (situacoes.isEmpty()) {
			Situacao situacao = new Situacao("Situacao 01");
			this.situacaoService.insert(situacao);			
		}
	}
	
	
	@Test
	@DisplayName("Teste do findById - Sucesso")
	public void testFindById_ok() {
		Optional<Situacao> situacao = this.situacaoService.findById(1L);
		assertTrue(situacao.isPresent());
	}
	
	@Test
	@DisplayName("Teste do findByDescricao - Sucesso")
	public void testFindByDescricao_ok() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 01");
		assertTrue(!situacoes.isEmpty());
	}
	
	@Test
	@DisplayName("Teste do findByDescricao - Falha")
	public void testFindByDescricao_fail() {
		List<Situacao> situacoes = this.situacaoService.findByDescricao("Situacao 0");
		assertTrue(situacoes.isEmpty());
	}
	
	
}
