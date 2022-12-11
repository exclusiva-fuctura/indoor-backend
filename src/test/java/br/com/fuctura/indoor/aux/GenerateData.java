package br.com.fuctura.indoor.aux;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.repositories.NoticiaRepository;
import br.com.fuctura.indoor.repositories.SituacaoRepository;

public class GenerateData {

	public static Situacao createSituacao(String descricao, SituacaoRepository situacaoRepository) {
		Situacao s1 = new Situacao();
		s1.setDescricao(descricao);
		
		// checa se existe
		List<Situacao> situacoes = situacaoRepository.findByDescricao(descricao);
		if (situacoes.isEmpty()) {
			situacaoRepository.save(s1);			
		} else {
			s1 = situacoes.get(0);
		}
		
		return s1;
	}
	
	public static void createNoticia(String descricao, Situacao situacao, NoticiaRepository noticiaRepository) {
		LocalDateTime fim = LocalDateTime.now().plusMinutes(60);		
		Noticia noticia = new Noticia("Titulo " + descricao, descricao, LocalDateTime.now(), fim, 20);		
		noticia.setSituacao(situacao);		
		
		List<Noticia> noticias = noticiaRepository.findByDescricao(descricao);
		if (noticias.isEmpty()) {
			noticiaRepository.save(noticia);			
		} 
	}
}
