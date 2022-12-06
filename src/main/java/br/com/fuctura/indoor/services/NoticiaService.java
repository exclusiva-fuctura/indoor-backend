package br.com.fuctura.indoor.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.repositories.NoticiaRepository;

@Service
public class NoticiaService {
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	@Autowired
	private SituacaoService situacaoService;
	
	/***
	 * Recupera uma noticia existente 
	 * @param id
	 * @return
	 */
	public Optional<Noticia> findById(Long id) {
		return this.noticiaRepository.findById(id);
	}
	
	/**
	 * Cria uma nova noticia 
	 * @param noticia
	 */
	public void insert(Noticia noticia) {
	
		this.noticiaRepository.save(noticia);
	}
	
	/**
	 * Atualiza a noticia existente
	 * @param noticia
	 */
	public void update(Noticia noticia) {
		
		this.noticiaRepository.save(noticia);
	}

	/**
	 * Remove uma noticia existente
	 * @param id
	 */
	public void delete(Long id) {
		Optional<Noticia> obj = this.findById(id);
		if(obj.isPresent()) {
			this.noticiaRepository.delete(obj.get());			
		}
	}
}
