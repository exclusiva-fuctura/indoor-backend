package br.com.fuctura.indoor.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fuctura.indoor.dtos.MensagemDto;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/")
public class ApiController {

	@Value("${application-version}")
	private String appVersion;
	
	@GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<MensagemDto> index() {
		
		MensagemDto dto = new MensagemDto();
		dto.setStatus(HttpStatus.OK.value());
		dto.setDescricao("[VERSAO " + this.appVersion + "] Api do Sistema está online");
		dto.setPath("/api/");
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
		
	@GetMapping(value="/doc")
	public RedirectView redirectWagger() {		
		return new RedirectView("/api/swagger-ui.html");	
	}
}
