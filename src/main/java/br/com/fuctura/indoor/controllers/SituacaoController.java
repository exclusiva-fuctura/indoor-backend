package br.com.fuctura.indoor.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.indoor.dtos.SituacaoDto;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.services.SituacaoService;

@RestController
@RequestMapping("/situacao")
public class SituacaoController {

	@Autowired
	private SituacaoService situacaoService;
	
	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<SituacaoDto>> index() {
		
		List<Situacao> situacoes = this.situacaoService.findAll();
		
		if (situacoes.isEmpty()) {
			List<SituacaoDto> list = new ArrayList<>();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		
		return ResponseEntity.ok(situacoes.stream()
				.map( s -> s.toDto()).collect(Collectors.toList()));
	}
	
	@GetMapping(value="/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SituacaoDto> show(@PathVariable Long id) {
		Optional<Situacao> situacao = this.situacaoService.findById(id); 
		
		if(situacao.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(situacao.get().toDto());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
}
