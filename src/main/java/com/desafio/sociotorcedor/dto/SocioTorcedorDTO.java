package com.desafio.sociotorcedor.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.desafio.sociotorcedor.domain.SocioTorcedor;
import com.fasterxml.jackson.annotation.JsonFormat;

public class SocioTorcedorDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nomeCompleto;
	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	private Integer time;
	private Set<CampanhaDTO> campanhas = new HashSet<>();

	public SocioTorcedorDTO() {
	}

	public SocioTorcedorDTO(Integer id, String nomeCompleto, String email, LocalDate dataNascimento,
			Integer time) {
		this.id = id;
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.time = time;
	}

	public SocioTorcedorDTO(String nomeCompleto, String email, LocalDate dataNascimento, Integer time) {
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.time = time;
	}
	
	public SocioTorcedorDTO(String nomeCompleto, Integer id) {
		this.id = id;
		this.nomeCompleto = nomeCompleto;
	}

	public SocioTorcedorDTO(SocioTorcedor st) {
		this.id = st.getId();
		this.nomeCompleto = st.getNomeCompleto();
		this.email = st.getEmail();
		this.dataNascimento = st.getDataNascimento();
		this.time = st.getTime();
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
	
	public Set<CampanhaDTO> getCampanhas() {
		return campanhas;
	}
	
	public void setCampanhas(Set<CampanhaDTO> campanhas) {
		this.campanhas = campanhas;
	}
}
