package br.com.fuctura.indoor.dtos;

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
	private String inicio;
	private String fim;
	private int duracaoSegundos;
	private long situacao;
	private String mensagem;
	
	public NoticiaDto(Noticia noticia) {
		if (null != noticia) {			
			this.numero = noticia.getId();
			this.titulo = noticia.getTitulo();
			this.descricao = noticia.getDescricao();
			this.inicio = noticia.getInicio() != null ? noticia.getInicio().toString() : null;
			this.fim = noticia.getFim() != null ? noticia.getFim().toString() : null;
			this.duracaoSegundos = noticia.getDuracao();
		}
		if (null != noticia.getSituacao()) {
			this.situacao = noticia.getSituacao().getId();
		}
	}
}
