package br.com.fuctura.indoor.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.fuctura.indoor.dtos.NoticiaDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "noticias")
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="not_id")
	private Long id;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="not_sit_id")
	private Situacao situacao;
	
	public NoticiaDto toDto() {
		return new NoticiaDto(this);
	}
	
	public Noticia(String titulo, String descricao, LocalDateTime ini, 
			LocalDateTime fim, int duracao) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.inicio = ini;
		this.fim = fim;
		this.duracao = duracao;
	}
	
	public Noticia(Long id, String titulo, String descricao, LocalDateTime ini, 
			LocalDateTime fim, int duracao) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.inicio = ini;
		this.fim = fim;
		this.duracao = duracao;
	}

	public Noticia(String titulo, String descricao, LocalDateTime ini, 
			LocalDateTime fim, int duracao, Situacao situacao) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.inicio = ini;
		this.fim = fim;
		this.duracao = duracao;
		this.situacao = situacao;
	}

	
}
