package br.com.fuctura.indoor.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.indoor.dtos.NoticiaDto;
import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.exceptions.NoticiaExistsException;
import br.com.fuctura.indoor.exceptions.NoticiaNotFoundException;
import br.com.fuctura.indoor.exceptions.RequiredParamException;
import br.com.fuctura.indoor.exceptions.SituacaoEmptyException;
import br.com.fuctura.indoor.exceptions.SituacaoNotExistsException;
import br.com.fuctura.indoor.repositories.NoticiaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticiaService {
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	@Autowired
	private SituacaoService situacaoService;
	
	/**
	 * Listagem de todas as noticias
	 * @return lista de noticias
	 */
	public List<Noticia> findAll() {
		return this.noticiaRepository.findAll();
	}
	
	/**
	 * Listagem das noticias disponíveis para apresentar no letreiro
	 * @return lsita de noticias
	 */
	public List<Noticia> findDisponiveis() {
		return this.findDisponiveis();
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
			log.warn("[ALERTA] Noticia já existe");
			throw new NoticiaExistsException("Noticia já existe");
		}
		if (null == noticia.getSituacao()) {
			log.warn("[ALERTA] Nenhuma situacao informada");
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
		Optional<Noticia> noticiaFound = this.findById(noticia.getId());
		if (noticiaFound.isEmpty()) {
			throw new NoticiaNotFoundException("Noticia nao encontrada");
		}
		
		// realizar as alterações
		noticiaFound.get().setDescricao(noticia.getDescricao());
		noticiaFound.get().setDuracao(noticia.getDuracao());
		noticiaFound.get().setInicio(noticia.getInicio());
		noticiaFound.get().setFim(noticia.getFim());
		noticiaFound.get().setTitulo(noticia.getTitulo());
		noticiaFound.get().setSituacao(noticia.getSituacao());
		
		noticia = noticiaFound.get();
		
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
	
	public Noticia generateNoticia(NoticiaDto dto) throws RequiredParamException, SituacaoNotExistsException {
		
		if (dto.getTitulo().isBlank() || dto.getTitulo().length() < 10) {
			throw new RequiredParamException("Titulo inválido");
		}			
		
		// verificar se a situacao inicial existe
		if (!this.situacaoService.isExists("Aguardando Início")) {
			throw new SituacaoNotExistsException("Situação inicial 'Aguardando Início' não existe.");
		}
		
		return new Noticia(
				dto.getTitulo(),
				dto.getDescricao(),
				LocalDateTime.parse(dto.getInicio(), DateTimeFormatter.ISO_DATE_TIME),
				LocalDateTime.parse(dto.getFim(), DateTimeFormatter.ISO_DATE_TIME),
				dto.getDuracaoSegundos(),
				this.getSituacao("Aguardando Início"));

	}
	
	public Noticia convertDtoToNoticia(NoticiaDto dto) throws RequiredParamException, SituacaoNotExistsException {
		
		if (dto.getTitulo().isBlank() || dto.getTitulo().length() < 10) {
			throw new RequiredParamException("Titulo inválido");
		}	
		
		// verificar se a situacao inicial existe
		Optional<Situacao> situacao = this.situacaoService.findById(dto.getSituacao());
		if (situacao.isEmpty()) {
			throw new SituacaoNotExistsException("Situação informada não existe.");	
		}
		
		Noticia noticia = new Noticia(
				dto.getTitulo(),
				dto.getDescricao(),
				LocalDateTime.parse(dto.getInicio(), DateTimeFormatter.ISO_DATE_TIME),
				LocalDateTime.parse(dto.getFim(), DateTimeFormatter.ISO_DATE_TIME),
				dto.getDuracaoSegundos(),
				situacao.get());
		noticia.setId(dto.getNumero());
		
		return noticia;

	}
	
	public void checkNoticia(NoticiaDto dto) throws RequiredParamException {		
		if (null == dto.getTitulo()) {
			throw new RequiredParamException("O Titulo precisa ser informado");
		}
		if (null == dto.getDescricao()) {
			throw new RequiredParamException("A Descrição precisa ser informada");
		}
		if (null == dto.getInicio()) {
			throw new RequiredParamException("A Data de Início precisa ser informada");
		}
		if (null == dto.getFim()) {
			throw new RequiredParamException("A Data do Fim precisa ser informada");
		}
		if (dto.getDuracaoSegundos() == 0) {
			throw new RequiredParamException("A Duração precisa ser informada");
		}
		LocalDateTime inicio = LocalDateTime.parse(dto.getInicio(), DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime fim = LocalDateTime.parse(dto.getFim(), DateTimeFormatter.ISO_DATE_TIME);
		if (inicio.isAfter(fim)) {
			throw new RequiredParamException("A Data Fim não pode ser maior que a data de início");
		}
	}
	
	public void checkSituacao(Long id) throws SituacaoEmptyException, RequiredParamException {
		this.situacaoService.checkSituacao(id);
	}
	
	private Situacao getSituacao(String situacao) {
		List<Situacao> situacoes = this.situacaoService.findByDescricao(situacao);
		return situacoes.get(0);
	}
}
