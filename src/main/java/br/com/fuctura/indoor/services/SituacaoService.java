package br.com.fuctura.indoor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.exceptions.FoundChildException;
import br.com.fuctura.indoor.exceptions.SituacaoExistsException;
import br.com.fuctura.indoor.repositories.SituacaoRepository;

@Service
public class SituacaoService {

	@Autowired
	private SituacaoRepository situacaoRepository;
	
	/**
	 * Recupera uma situação apartir de um ID
	 * @param id
	 * @return Retorna uma instância de uma Situacao
	 */
	public Optional<Situacao> findById(Long id) {
		return this.situacaoRepository.findById(id);
	}
	
	/**
	 * Recupera uma lista de situações baseado na descricao
	 * @param descricao
	 * @return Lista de situações
	 */
	public List<Situacao> findByDescricao(String descricao) {
		return this.situacaoRepository.findByDescricao(descricao);
	}
	
	/**
	 * Cria uma nova situação
	 * @param situacao
	 */
	public void insert(Situacao situacao) throws SituacaoExistsException {
		if (null != situacao) {
			
			List<Situacao> situacoes = this.situacaoRepository.findByDescricao(situacao.getDescricao());
			
			if(!situacoes.isEmpty()) {				
				throw new SituacaoExistsException("Situacao já existe");
			}
						
			this.situacaoRepository.save(situacao);			
		}
	}
	
	/**
	 * Atualiza a situação existente
	 * @param situacao
	 */
	public void update(Situacao situacao) throws SituacaoExistsException {
		if (null != situacao && this.isSituacaoValid(situacao)) {
			// localizar o objeto no banco
			Optional<Situacao> sit = this.situacaoRepository.findById(situacao.getId());
			// encontrando persisto a nova situacao
			if(sit.isPresent()) {
				
				List<Situacao> situacoes = this.situacaoRepository.findByDescricao(situacao.getDescricao());
				
				if(!situacoes.isEmpty()) {				
					throw new SituacaoExistsException("Situacao já existe");
				}
				
				this.situacaoRepository.save(situacao);			
			}
		}
	}
	
	public void delete(Situacao situacao) throws FoundChildException {
		if (null != situacao) {
			// verificar se existe alguma noticia com situcao
			long exists = this.situacaoRepository.noticiasWithSituacao(situacao.getId());
			if (exists > 0) {
				throw new FoundChildException("Situação associado a alguma Noticia");
			}
			this.situacaoRepository.delete(situacao);			
		}
	}
	
	/**
	 * Verifica se os dados da situacao está válida
	 * @param situacao
	 * @return retorna true ou false
	 */
	private boolean isSituacaoValid(Situacao situacao) {
		// verifica se a descrição esta vazia ou com espaços em branco
		if(situacao.getDescricao().isBlank()) {
			return false;
		}
		
		// verifica se a descrição esta com menos de 5 caracteres
		if(situacao.getDescricao().length() < 5) {
			return false;
		}
		
		return true;
	}

	/**
	 * Retorna lista de situacoes cadastradas
	 * @return lista de situações
	 */
	public List<Situacao> findAll() {
		return this.situacaoRepository.findAll();		
	}
	
	/**
	 * Verifica se exite a Situacao informada
	 * @param situacao
	 * @return retorna verdadeiro ou falso
	 */
	public boolean isExists(Situacao situacao) {
		List<Situacao> situacoes = this.findByDescricao(situacao.getDescricao());
		return !situacoes.isEmpty();
	}
}
