package br.com.fuctura.indoor.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.fuctura.indoor.dtos.SituacaoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Situacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sit_id")
	private long id;
	@Column(name = "sit_descricao")
	private String descricao;
	
	public SituacaoDto toDto() {
		return new SituacaoDto(this);
	}
}
