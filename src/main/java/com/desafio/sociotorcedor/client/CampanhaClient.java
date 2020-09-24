package com.desafio.sociotorcedor.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.desafio.sociotorcedor.domain.SocioTorcedor;
import com.desafio.sociotorcedor.dto.CampanhaDTO;

@Component
public class CampanhaClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(CampanhaClient.class);
	
	@Value("${url.campanhas}")
	private String url;
	
	private RestTemplate template;
	
	@Autowired
	public CampanhaClient(RestTemplateBuilder builder) {
		this.template = builder.build();
	}
	
	@Retryable( value = RuntimeException.class, 
		      maxAttempts = 5, backoff = @Backoff(delay = 1000))
	public List<CampanhaDTO> buscarTodos() {
		ResponseEntity<CampanhaDTO[]> response = template.getForEntity(url, CampanhaDTO[].class);
		CampanhaDTO[] dtos = response.getBody();
		return Arrays.asList(dtos);
	}
	
	@Retryable( value = RuntimeException.class, 
		      maxAttempts = 5, backoff = @Backoff(delay = 1000))
	public List<CampanhaDTO> buscarNovasCampanhas(Integer id) {
		String urlCreated = new StringBuilder(url).append("/novas-por-time").append("/").append(id).toString();
		ResponseEntity<CampanhaDTO[]> response = template.getForEntity(urlCreated, CampanhaDTO[].class);
		CampanhaDTO[] dtos = response.getBody();
		return Arrays.asList(dtos);
	}
	
	@Retryable( value = RuntimeException.class, 
		      maxAttempts = 5, backoff = @Backoff(delay = 1000))
	public void associar(List<CampanhaDTO> campanhas, SocioTorcedor socio) {
		String urlCreated = new StringBuilder(url).append("/associar").toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlCreated)
		        .queryParam("socio", socio.getId())
		        .queryParam("time", socio.getTime());
		List<Integer> ids = campanhas.stream().map(CampanhaDTO::getId).collect(Collectors.toList());
		template.postForEntity(builder.toUriString(), ids, Void.class);
	}
	
	@Retryable( value = RuntimeException.class, 
		      maxAttempts = 5, backoff = @Backoff(delay = 1000))
	public Set<CampanhaDTO> campanhasPorSocio(SocioTorcedor socio) {
		String urlCreated = new StringBuilder(url).append("/por-socio").toString();
		String uriString = UriComponentsBuilder.fromUriString(urlCreated)
		        .queryParam("socio", socio.getId()).toUriString();
		ResponseEntity<CampanhaDTO[]> response = template.getForEntity(uriString, CampanhaDTO[].class);
		return new HashSet<CampanhaDTO>(Arrays.asList(response.getBody()));
	}
	
	@Recover
	private List<CampanhaDTO> buscarTodosFallback(Throwable e) {
		LOG.info("Serviço de campanhas indisponível");
		return Collections.emptyList();
    }
	
	@Recover
	public List<CampanhaDTO> buscarNovasCampanhasFallback(Integer id) {
		LOG.info("Serviço de campanhas indisponível");
		return Collections.emptyList();
    }
	
	@Recover
	public void associarFallback(List<CampanhaDTO> campanhas, SocioTorcedor socio) {
		LOG.info("Serviço de campanhas indisponível");
	}
	
	@Recover
	public Set<CampanhaDTO> campanhasPorSocioFallback(SocioTorcedor socio) {
		LOG.info("Serviço de campanhas indisponível");
		return Collections.emptySet();
	}
}
