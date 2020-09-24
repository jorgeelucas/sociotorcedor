package com.desafio.sociotorcedor.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.desafio.sociotorcedor.client.CampanhaClient;
import com.desafio.sociotorcedor.domain.SocioTorcedor;
import com.desafio.sociotorcedor.dto.CampanhaDTO;
import com.desafio.sociotorcedor.dto.SocioTorcedorDTO;
import com.desafio.sociotorcedor.exception.ObjectNotFoundException;
import com.desafio.sociotorcedor.exception.UsuarioJaExisteException;
import com.desafio.sociotorcedor.repository.SocioTorcedorRepository;
import com.desafio.sociotorcedor.utils.DateUtils;

@Service
public class SocioTorcedorService {

	@Autowired
	private SocioTorcedorRepository socioTorcedorRepository;
	
	@Autowired
	private CampanhaClient campanhaClient;
	
	@Cacheable(value = "socioTorcedorGetAll", unless = "#result==null")
	public List<SocioTorcedorDTO> getAll() {
		List<SocioTorcedor> all = socioTorcedorRepository.findAll();
		return all.stream().map(st -> new SocioTorcedorDTO(st)).collect(Collectors.toList());
	}

	@Caching(evict = {@CacheEvict(value = "socioTorcedorGetById", allEntries = true),
			@CacheEvict(value = "socioTorcedorGetAll", allEntries = true) })
	public SocioTorcedor create(SocioTorcedor socioTorcedor) {
		if (socioTorcedor.getId() == null) {
			validarCadastro(socioTorcedor);
		}
		return socioTorcedorRepository.save(socioTorcedor);
	}

	@Cacheable(value = "socioTorcedorGetById", key = "#id", unless = "#result==null")
	public SocioTorcedorDTO findById(Integer id) {
		SocioTorcedor socioTorcedor = socioTorcedorRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Socio de id " + id + " não encontrado"));
		Set<CampanhaDTO> campanhas = campanhaClient.campanhasPorSocio(socioTorcedor);
		SocioTorcedorDTO dto = new SocioTorcedorDTO(socioTorcedor);
		dto.setCampanhas(campanhas);
		return dto;
	}

	public Page<SocioTorcedorDTO> getPagination(String nomeCompleto, String email, Integer time, String dataAniversario,
			PageRequest pageRequest) {
		LocalDate aniversario = DateUtils.parseDate(dataAniversario);

		Example<SocioTorcedor> exampleQuery = matcherCreator(nomeCompleto, email, time, aniversario);
		Page<SocioTorcedor> page = socioTorcedorRepository.findAll(exampleQuery, pageRequest);
		return page.map(p -> new SocioTorcedorDTO(p));
	}

	@Caching(evict = {@CacheEvict(value = "socioTorcedorGetById", allEntries = true),
			@CacheEvict(value = "socioTorcedorGetAll", allEntries = true) })
	public SocioTorcedorDTO update(SocioTorcedorDTO socioTorcedorDTO, Integer id) {
		SocioTorcedor socioTorcedor = new SocioTorcedor(socioTorcedorDTO);
		socioTorcedor.setId(id);
		SocioTorcedor socioTorcedorUpdated = socioTorcedorRepository.save(socioTorcedor);
		return new SocioTorcedorDTO(socioTorcedorUpdated);
	}
	
	@Caching(evict = {@CacheEvict(value = "socioTorcedorGetById", allEntries = true),
			@CacheEvict(value = "socioTorcedorGetAll", allEntries = true) })
	public void deleteById(Integer id) {
		findById(id);
		socioTorcedorRepository.deleteById(id);
	}

	private Example<SocioTorcedor> matcherCreator(String nomeCompleto, String email, Integer idTime,
			LocalDate dataAniversario) {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("nomeCompleto", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains()).withIgnoreCase("nomeCompleto")
				.withIgnoreCase("email").withIgnoreNullValues();
		SocioTorcedor SocioTorcedorSearch = new SocioTorcedor(nomeCompleto, email, dataAniversario, idTime);
		return Example.of(SocioTorcedorSearch, matcher);
	}
	
	private void validarCadastro(SocioTorcedor socioTorcedor) {
		String email = socioTorcedor.getEmail();
		if (socioTorcedorRepository.existsByEmail(email)) {
			throw new UsuarioJaExisteException("email já cadastrado.");
		}
	}
	
//	private void validaTime(Time time) {
//		timeService.isTimeValido(time.getId());
//	}

}
