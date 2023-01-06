package br.com.fuctura.indoor.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.indoor.config.SwaggerConfig;
import br.com.fuctura.indoor.dtos.SituacaoDto;
import br.com.fuctura.indoor.entities.Situacao;
import br.com.fuctura.indoor.exceptions.FoundChildException;
import br.com.fuctura.indoor.exceptions.SituacaoExistsException;
import br.com.fuctura.indoor.services.SituacaoService;
import br.com.fuctura.indoor.utils.NumberUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {SwaggerConfig.API_SITUACAO})
@RestController
@RequestMapping("/situacao")
public class SituacaoController {

	@Autowired
	private SituacaoService situacaoService;
	
	@ApiOperation(value = "Listagem de todas as situações")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),	
	})
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<SituacaoDto>> index() {
		
		List<Situacao> situacoes = this.situacaoService.findAll();
		
		if (situacoes.isEmpty()) {
			List<SituacaoDto> list = new ArrayList<>();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		
		return ResponseEntity.ok(situacoes.stream()
				.map( s -> s.toDto()).collect(Collectors.toList()));
	}
	
	@ApiOperation(value = "Recupera uma situação baseada no id")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 400, message = "Erro nos parametros da requisição"),
	    @ApiResponse(code = 404, message = "Situação não encontrada"),	
	})	
	@GetMapping(value="/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SituacaoDto> show(
			@PathVariable @ApiParam(value = "Id da situação", required=true, defaultValue = "0") String id) {
		
		if (!NumberUtils.isNumeric(id) || id == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Optional<Situacao> situacao = this.situacaoService.findById(Long.parseLong(id)); 
		
		if(situacao.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(situacao.get().toDto());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ApiOperation(value = "Cria uma nova situação")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "Situação criada"),
	    @ApiResponse(code = 400, message = "Erro nos parametros da requisição"),
	    @ApiResponse(code = 403, message = "Operação não permitida. Situação já existe"),	
	})	
	@PostMapping(produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SituacaoDto> store(			
		@RequestBody @ApiParam(value = "Nova Situação", required=true) SituacaoDto dto) {		

		// verificar se existe descricao
		if (null == dto.getDescricao() || dto.getDescricao().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		// verificar se existe algum id e se já existe
		if (null != dto.getId()) {
			Optional<Situacao> situacao = this.situacaoService.findById(dto.getId());
			
			if(situacao.isPresent()) {				
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
			}
		}				
		
		// persitir no banco
		Situacao novo = new Situacao(dto.getDescricao());
		try {
			this.situacaoService.insert(novo);
		} catch (SituacaoExistsException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
			
		return ResponseEntity.status(HttpStatus.CREATED).body(novo.toDto());	
	
	}
	
	@ApiOperation(value = "Atualiza uma situação baseada no id")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 400, message = "Erro nos parametros da requisição"),
	    @ApiResponse(code = 403, message = "Operação não permitida. Situação já existe"),
	    @ApiResponse(code = 404, message = "Situação não encontrada"),	
	})	
	@PutMapping(value="/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SituacaoDto> update(			
		@PathVariable @ApiParam(value = "Id da situação", required=true, defaultValue = "0") String id,
		@RequestBody @ApiParam(value = "Situação com a alteração", required=true) SituacaoDto dto) {
		
		if (!NumberUtils.isNumeric(id) || id == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Optional<Situacao> situacao = this.situacaoService.findById(Long.parseLong(id)); 

		if (null == dto.getDescricao() || dto.getDescricao().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		if (Long.parseLong(id) != dto.getId()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();	
		}
		
		if(situacao.isPresent()) {
			// atualiza a descricao
			situacao.get().setDescricao(dto.getDescricao());
			// persitir no banco
			try {
				this.situacaoService.update(situacao.get());
			} catch (SituacaoExistsException e) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(situacao.get().toDto());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ApiOperation(value = "Remove uma situação baseada no id")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 400, message = "Erro nos parametros da requisição"),
	    @ApiResponse(code = 403, message = "Operação não permitida. Situação associado a uma notícia"),
	    @ApiResponse(code = 404, message = "Situação não encontrada"),	
	})	
	@DeleteMapping(value="/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SituacaoDto> delete(			
		@PathVariable @ApiParam(value = "Id da situação", required=true, defaultValue = "0") String id) {

		if (!NumberUtils.isNumeric(id) || id == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Optional<Situacao> situacao = this.situacaoService.findById(Long.parseLong(id)); 

		if(situacao.isPresent()) {
			// remove a situacao
			try {
				this.situacaoService.delete(situacao.get());
			} catch (FoundChildException e) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(situacao.get().toDto());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
}
