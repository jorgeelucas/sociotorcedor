package com.desafio.sociotorcedor.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.desafio.sociotorcedor.dto.CampanhaDTO;
import com.desafio.sociotorcedor.dto.SocioTorcedorDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class SocioTorcedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@NotBlank(message = "Nome obrigatório")
	@Column(name = "NOME_COMPLETO")
	private String nomeCompleto;

	@Email(message = "Um email valido deve ser cadastrado")
	@Column(name = "EMAIL")
	private String email;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "DT_NASCIMENTO")
	private LocalDate dataNascimento;

	private Integer time;

	@Transient
	private Set<Integer> campanhas = new HashSet<>();

	public SocioTorcedor() {
	}

	public SocioTorcedor(String nomeCompleto, String email, LocalDate dataNascimento, Integer time) {
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.time = time;
	}

	public SocioTorcedor(SocioTorcedorDTO dto) {
		this.id = dto.getId();
		this.nomeCompleto = dto.getNomeCompleto();
		this.email = dto.getEmail();
		this.dataNascimento = dto.getDataNascimento();
		this.time = dto.getTime();
		this.campanhas = dto.getCampanhas().stream().map(CampanhaDTO::getId).collect(Collectors.toSet());
	}

	public SocioTorcedor(Integer id, String nome) {
		this.id = id;
		this.nomeCompleto = nome;
	}
	
	public SocioTorcedor(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Set<Integer> getCampanhas() {
		return campanhas;
	}

	public void setCampanhas(Set<Integer> campanhas) {
		this.campanhas = campanhas;
	}
}
