package br.com.fuctura.indoor.dtos;

import br.com.fuctura.indoor.entities.Situacao;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SituacaoDto {
	
	private Long id;	
	private String descricao;
	
	public SituacaoDto(Situacao situacao) {
		if (null != situacao) {
			this.id = situacao.getId();
			this.descricao = situacao.getDescricao();			
		}
	}
}
