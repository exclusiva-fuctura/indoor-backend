package br.com.fuctura.indoor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.exceptions.NoticiaExistsException;
import br.com.fuctura.indoor.exceptions.NoticiaNotFoundException;
import br.com.fuctura.indoor.exceptions.SituacaoEmptyException;
import br.com.fuctura.indoor.repositories.NoticiaRepository;

@Service
public class NoticiaService {
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	/**
	 * Listagem de todas as noticias
	 * @return lista de noticias
	 */
	public List<Noticia> findAll() {
		return this.noticiaRepository.findAll();
	}
	
	/***
	 * Recupera uma noticia existente 
	 * @param id
	 * @return noticia
	 */
	public Optional<Noticia> findById(Long id) {
		return this.noticiaRepository.findById(id);
	}
	
	/**
	 * Lista de noticias filtrado pelo titulo
	 * @param string
	 * @return lista de noticias
	 */
	public List<Noticia> findByTitulo(String string) {
		return this.noticiaRepository.findByTitulo(string);
	}

	/**
	 * Lista de noticias filtrado pela descricao
	 * @param string
	 * @return lista de noticias
	 */
	public List<Noticia> findByDescricao(String string) {
		return this.noticiaRepository.findByDescricao(string);
	}
	
	/**
	 * Cria uma nova noticia 
	 * @param noticia
	 * @throws NoticiaExistsException 
	 * @throws SituacaoEmptyException 
	 */
	public void insert(Noticia noticia) throws NoticiaExistsException, SituacaoEmptyException {
		if (this.isExists(noticia)) {
			throw new NoticiaExistsException("Noticia já existe");
		}
		if (null == noticia.getSituacao()) {
			throw new SituacaoEmptyException("Nenhuma situacao informada");
		}
		this.noticiaRepository.save(noticia);
	}
	
	/**
	 * Atualiza a noticia existente
	 * @param noticia
	 * @throws NoticiaNotFoundException 
	 * @throws TituloExistsException 
	 */
	public void update(Noticia noticia) throws NoticiaExistsException, NoticiaNotFoundException {
		if (this.isExists(noticia)) {
			throw new NoticiaExistsException("Noticia já existe");
		}
		Optional<Noticia> noticiaFound = this.findById(noticia.getId());
		if (noticiaFound.isPresent()) {
			throw new NoticiaNotFoundException("Noticia nao encontrada");
		}
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
	
	/**
	 * Verifica se existe alguma noticia com o titulo ou descricao passada
	 * @param titulo
	 * @return verdadeiro ou falso
	 */
	public boolean isExists(Noticia noticia) {
		return !this.findByTitulo(noticia.getTitulo()).isEmpty() || 
			   !this.findByDescricao(noticia.getDescricao()).isEmpty();
	}
}
