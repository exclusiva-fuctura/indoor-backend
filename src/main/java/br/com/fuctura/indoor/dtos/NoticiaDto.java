package br.com.fuctura.indoor.dtos;

import java.time.LocalDateTime;

import br.com.fuctura.indoor.entities.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticiaDto {

	private long numero;
	private String titulo;
	private String descricao;
	private LocalDateTime inicio;
	private LocalDateTime fim;
	private int duracao_segundos;
	
	public NoticiaDto(Noticia noticia) {
		if (null != noticia) {			
			this.numero = noticia.getId();
			this.titulo = noticia.getTitulo();
			this.descricao = noticia.getDescricao();
			this.inicio = noticia.getInicio();
			this.fim = noticia.getFim();
			this.duracao_segundos = noticia.getDuracao();
		}
	}
}
