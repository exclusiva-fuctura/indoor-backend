package br.com.fuctura.indoor.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.indoor.entities.Situacao;
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
	 * Cria uma nova situação
	 * @param situacao
	 */
	public void insert(Situacao situacao) {
		if (null != situacao) {
			this.situacaoRepository.save(situacao);			
		}
	}
	
	/**
	 * Atualiza a situação existente
	 * @param situacao
	 */
	public void update(Situacao situacao) {
		if (null != situacao && this.isSituacaoValid(situacao)) {
			// localizar o objeto no banco
			Optional<Situacao> sit = this.situacaoRepository.findById(situacao.getId());
			// encontrando persisto a nova situacao
			if(sit.isPresent()) {				
				this.situacaoRepository.save(situacao);			
			}
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
}
