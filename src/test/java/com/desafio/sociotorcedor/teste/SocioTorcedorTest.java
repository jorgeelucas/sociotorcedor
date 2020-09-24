package com.desafio.sociotorcedor.teste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.desafio.sociotorcedor.SocioTorcedorApplication;
import com.desafio.sociotorcedor.domain.SocioTorcedor;
import com.desafio.sociotorcedor.dto.SocioTorcedorDTO;
import com.desafio.sociotorcedor.repository.SocioTorcedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocioTorcedorApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SocioTorcedorTest {
	
	private MockMvc mockMvc;
	
	@MockBean
	private SocioTorcedorRepository repository;
    
	@Autowired
	WebApplicationContext webAppCtx;
	
    @Autowired
    private ObjectMapper objectMapper;
    
    @Before
	public void setUp() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppCtx).build();
    	
    	when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(new SocioTorcedor(1, "obj_encontrado")));
    	when(repository.save(Mockito.any(SocioTorcedor.class))).thenReturn(new SocioTorcedor(1));
    	doNothing().when(repository).deleteById(Mockito.anyInt());
	}

    @Test
    public void testaCriacaoDeNovoSocioTorcedor() throws Exception {
    	LocalDate birthDay = LocalDate.of(1999, Month.FEBRUARY, 21);
    	SocioTorcedorDTO dto = new SocioTorcedorDTO("teste", "email@email.com", birthDay, 1);
    	mockMvc.perform(post("/desafio/v1/sociostorcedores")
    	        .contentType("application/json")
    	        .content(objectMapper.writeValueAsString(dto)))
    	        .andExpect(status().isCreated());
    }
    
    @Test
    public void testaErroDeEmailJaExistente() throws Exception {
    	SocioTorcedorDTO dto = new SocioTorcedorDTO("outro teste", "email@email.com", LocalDate.of(1999, Month.JANUARY, 11), 1);
    	when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
    	mockMvc.perform(post("/desafio/v1/sociostorcedores")
    	        .contentType("application/json")
    	        .content(objectMapper.writeValueAsString(dto)))
    	        .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testaBuscarPorId() throws Exception {
    	MvcResult result = mockMvc.perform(get("/desafio/v1/sociostorcedores/1")
    	        .contentType("application/json"))
    	        .andExpect(status().isOk())
    	        .andExpect(content().contentType("application/json"))
    	        .andReturn();
    	JSONObject jsonObject = objectMapper.convertValue(result.getResponse().getContentAsString(), JSONObject.class);
    	assertThat(jsonObject.get("nomeCompleto")).isEqualTo("obj_encontrado");
    }
    
    @Test
    public void testaDeletar() throws Exception {
    	mockMvc.perform(delete("/desafio/v1/sociostorcedores/1")
    	        .contentType("application/json"))
    	        .andExpect(status().isOk());
    }
    
}
