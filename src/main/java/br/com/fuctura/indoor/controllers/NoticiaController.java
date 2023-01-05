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
import br.com.fuctura.indoor.dtos.NoticiaDto;
import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.exceptions.NoticiaExistsException;
import br.com.fuctura.indoor.exceptions.NoticiaNotFoundException;
import br.com.fuctura.indoor.exceptions.RequiredParamException;
import br.com.fuctura.indoor.exceptions.SituacaoEmptyException;
import br.com.fuctura.indoor.exceptions.SituacaoNotExistsException;
import br.com.fuctura.indoor.services.NoticiaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {SwaggerConfig.API_NOTICIA})
@RestController
@RequestMapping("/noticia")
public class NoticiaController {

	@Autowired 
	private NoticiaService noticiaService;
	
	@ApiOperation(value = "Listagem de todas as noticias")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),	
	})
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<NoticiaDto>> index() {
		
		List<Noticia> noticias = this.noticiaService.findAll();
		
		if (noticias.isEmpty()) {
			List<NoticiaDto> list = new ArrayList<>();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		
		return ResponseEntity.ok(noticias.stream()
				.map( s -> s.toDto()).collect(Collectors.toList()));
	}
	
	@ApiOperation(value = "Listagem de todas as noticias")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 404, message = "Noticia não encontrada"),
	})
	@GetMapping(value="/{id}" ,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<NoticiaDto> show(
			@PathVariable @ApiParam(value = "Id da noticia", required=true) Long id) {
		
		Optional<Noticia> noticia = this.noticiaService.findById(id);
		
		if (noticia.isPresent()) {			
			return ResponseEntity.status(HttpStatus.OK).body(noticia.get().toDto());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ApiOperation(value = "Criar a noticia")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 400, message = "Parâmetros incompletos"),
	    @ApiResponse(code = 403, message = "Operação não permitida"),
	})
	@PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<NoticiaDto> store(
			@RequestBody @ApiParam(value = "Objeto a ser criado", required=true) NoticiaDto dto) {
				
		 
		Noticia noticia;
		try {
			this.noticiaService.checkNoticia(dto);
			noticia = this.noticiaService.generateNoticia(dto);
			this.noticiaService.insert(noticia);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(noticia.toDto());
		} catch (RequiredParamException | SituacaoEmptyException | SituacaoNotExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		} catch (NoticiaExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);	
		}
	}
	
	@ApiOperation(value = "Atualiza a noticia")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Solicitação atendida com sucesso"),
	    @ApiResponse(code = 400, message = "Parâmetros incompletos"),
	    @ApiResponse(code = 403, message = "Operação não permitida"),
	})
	@PutMapping(value="/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<NoticiaDto> update(
			@RequestBody @ApiParam(value = "Objeto a ser atualizado", required=true) NoticiaDto dto,
			@PathVariable @ApiParam(value = "Id da noticia", required=true) Long id ) {
				
		if (null == id || dto.getNumero() != id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}

		Noticia noticia;
		try {
			this.noticiaService.checkNoticia(dto);
			this.noticiaService.checkSituacao(dto.getSituacao());
			// converte o DTO em uma noticia
			noticia = this.noticiaService.convertDtoToNoticia(dto);
			this.noticiaService.update(noticia);
			
			return ResponseEntity.status(HttpStatus.OK).body(noticia.toDto());
		} catch (RequiredParamException | SituacaoEmptyException e) {	
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		} catch (NoticiaExistsException | SituacaoNotExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
		} catch (NoticiaNotFoundException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
		}		
	}
	
	@ApiOperation(value = "Remover a noticia")
	@ApiResponses(value = {
	    @ApiResponse(code = 204, message = "Solicitação atendida com sucesso sem corpo"),
	    @ApiResponse(code = 400, message = "Parâmetros incompletos"),
	})
	@DeleteMapping(value="/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<NoticiaDto> delete(			
			@PathVariable @ApiParam(value = "Id da noticia", required=true) Long id ) {
				
		if (null == id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		this.noticiaService.delete(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
		
	}
}
