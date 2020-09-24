package com.desafio.sociotorcedor.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.desafio.sociotorcedor.client.CampanhaClient;
import com.desafio.sociotorcedor.domain.SocioTorcedor;
import com.desafio.sociotorcedor.dto.CampanhaDTO;
import com.desafio.sociotorcedor.dto.SocioTorcedorDTO;
import com.desafio.sociotorcedor.service.SocioTorcedorService;
import com.desafio.sociotorcedor.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Socios torcedores")
@RestController
@RequestMapping("desafio/v1/sociostorcedores")
public class SocioTorcedorRestController {

	@Autowired
	private SocioTorcedorService socioTorcedorService;
	
	@Autowired
	private CampanhaClient campanhaClient;
	
	@ApiOperation(value="Obter todos", notes="Busca todos os socios torcedores.")
	@GetMapping
	public ResponseEntity<List<SocioTorcedorDTO>> getAll() {
		return ResponseEntity.ok().body(socioTorcedorService.getAll());
	}

	@ApiOperation(value="Obter todos paginado", notes="Busca todos os socios torcedores paginado.")
	@GetMapping("/page")
	public ResponseEntity<Page<SocioTorcedorDTO>> page(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "time", required = false) Integer time,
			@RequestParam(value = "data_aniversario", defaultValue = "") String dataAniversario,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nomeCompleto") String orderBy,
			@RequestParam(value = "directions", defaultValue = "ASC") String directions) {

		nome = StringUtils.decodeName(nome);
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directions), orderBy);
		Page<SocioTorcedorDTO> pagination = socioTorcedorService.getPagination(nome, email, time, dataAniversario,
				pageRequest);
		return ResponseEntity.ok().body(pagination);
	}

	@ApiOperation(value="Criar novo", notes="Cria um novo socio torcedor.")
	@PostMapping
	public ResponseEntity<List<CampanhaDTO>> create(@RequestBody @Valid SocioTorcedor socioTorcedor) {
		SocioTorcedor created = socioTorcedorService.create(socioTorcedor);
		List<CampanhaDTO> campanhas = campanhaClient.buscarNovasCampanhas(socioTorcedor.getTime());
		campanhaClient.associar(campanhas, socioTorcedor);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
		return ResponseEntity.created(uri).body(campanhas);
	}

	@ApiOperation(value="Obter socio torcedor por id", notes="Detalha um socio torcedor por id.")
	@GetMapping("/{id}")
	public ResponseEntity<SocioTorcedorDTO> findById(@PathVariable("id") Integer id) {
		return ResponseEntity.ok().body(socioTorcedorService.findById(id));
	}

	@ApiOperation(value="Alterar por id", notes="Altera um socio torcedor existente por id.")
	@PutMapping("/{id}")
	public ResponseEntity<SocioTorcedorDTO> update(@RequestBody SocioTorcedorDTO socioTorcedorDTO,
			@PathVariable("id") Integer id) {
		return ResponseEntity.ok().body(socioTorcedorService.update(socioTorcedorDTO, id));
	}
	
	@ApiOperation(value="Deletar por id", notes="Deleta um socio torcedor por id.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Integer id) {
		socioTorcedorService.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
