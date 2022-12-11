package br.com.fuctura.indoor.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MensagemDto {

	int status;
	String descricao;	
	String path;
	LocalDateTime data;
	
	public MensagemDto() {
		this.data = LocalDateTime.now();
	}
}
