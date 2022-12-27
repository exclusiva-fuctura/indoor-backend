package br.com.fuctura.indoor.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fuctura.indoor.entities.Noticia;
import br.com.fuctura.indoor.exceptions.RequiredParamException;
import br.com.fuctura.indoor.services.NoticiaService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoticiaController.class)
class NoticiaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private NoticiaService noticiaService;
	
	private List<Noticia> noticias;
	
	@BeforeEach
	public void setUp() {
		LocalDateTime data = LocalDateTime.now();
		noticias = new ArrayList<>();
		noticias.add(new Noticia(1L, "Titulo 10","Descr 10", data, data.plusMinutes(30), 30));
		noticias.add(new Noticia(2L, "Titulo 20","Descr 20", data.plusMinutes(20), data.plusHours(1L), 20));
		noticias.add(new Noticia(3L, "Titulo 30","Descr 30", null, null, 20));
	}
	
	@Test
	@DisplayName("Teste de Listagem de noticias")
	void testIndex() throws Exception {
		when(noticiaService.findAll()).thenReturn(noticias);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/noticia")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").isNotEmpty())
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").value("Titulo 10"));
	}
	
	@Test
	@DisplayName("Teste de obter uma noticia")
	void testShow() throws Exception {
				
		when(noticiaService.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(noticias.get(0)));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/noticia/2")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Titulo 10"));
	}
	
	@Test
	@DisplayName("Teste de criar uma noticia")
	void testStore() throws Exception {
				
		when(noticiaService.generateNoticia(Mockito.any()))
			.thenReturn(noticias.get(0));
		doNothing().when(noticiaService).insert(Mockito.any());
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/noticia")
				.content(asJsonString(noticias.get(2)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Titulo 10"));
	}
	
	@Test
	@DisplayName("Teste de criar uma noticia - Falha")
	void testStore_fail() throws Exception {
				
		when(noticiaService.generateNoticia(Mockito.any()))
			.thenThrow(new RequiredParamException(""));
		doNothing().when(noticiaService).insert(Mockito.any());
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/noticia")
				.content(asJsonString(noticias.get(2)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isBadRequest());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
