package br.com.fuctura.indoor.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.fuctura.indoor.dtos.NoticiaDto;
import lombok.Getter;

@Getter
@Entity
@Table(name = "noticias")
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="not_id")
	private long id;
	@Column(name="not_titulo")	
	private String titulo;
	@Column(name="not_descricao")
	private String descricao;
	@Column(name="not_inicio")
	private LocalDateTime inicio;
	@Column(name="not_fim")
	private LocalDateTime fim;
	@Column(name="not_duracao")
	private int duracao;
	
	public NoticiaDto toDto() {
		return new NoticiaDto(this);
	}
	
}
