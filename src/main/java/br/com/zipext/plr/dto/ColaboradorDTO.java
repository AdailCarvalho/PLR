package br.com.zipext.plr.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.zipext.plr.model.ColaboradorCargoModel;
import br.com.zipext.plr.model.ColaboradorMetaEspecificaModel;
import br.com.zipext.plr.model.ColaboradorMetaGeralModel;
import br.com.zipext.plr.model.ColaboradorModel;
import br.com.zipext.plr.model.MetaEspecificaModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColaboradorDTO {
	
	private Long id;
	private String nome;
	private String descricao;
	private String matricula;
	private Character situacao;
	
	private CargoDTO cargo;
	private List<MetasDTO> metasGerais;
	private List<MetasDTO> metasProjetos;
	private List<MetasDTO> metasQuantitativas;
	
	public ColaboradorDTO() {}
	
	public ColaboradorDTO(ColaboradorModel model) {
		BeanUtils.copyProperties(model, this);
		this.cargo = new CargoDTO((ColaboradorCargoModel) model.getColaboradoresCargos().toArray()[0]);
		this.metasGerais = new ArrayList<>();
		this.metasProjetos = new ArrayList<>();
		this.metasQuantitativas = new ArrayList<>();
		
		Set<ColaboradorMetaGeralModel> metasGerais = model.getColaboradoresMetasGerais();
		if (metasGerais != null && !metasGerais.isEmpty()) {
			metasGerais.forEach(meta -> this.metasGerais.add(new MetasDTO(meta)));
		}
		
		Set<ColaboradorMetaEspecificaModel> metasEspecificas = model.getColaboradoresMetasEspecificas();
		if (metasEspecificas != null && !metasEspecificas.isEmpty()) {
			metasEspecificas.stream().filter(meta -> meta.getPk().getMetaEspecifica().equals(new MetaEspecificaModel(1L)))
					.forEach(meta -> this.metasQuantitativas.add(new MetasDTO(meta)));
			
			metasEspecificas.stream().filter(meta -> meta.getPk().getMetaEspecifica().equals(new MetaEspecificaModel(2L)))
					.forEach(meta -> this.metasProjetos.add(new MetasDTO(meta)));
		}
	}
	
	@JsonIgnore
	public ColaboradorModel getModel() {
		ColaboradorModel model = new ColaboradorModel();
		BeanUtils.copyProperties(this, model);
		
		return model;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Character getSituacao() {
		return situacao;
	}

	public void setSituacao(Character situacao) {
		this.situacao = situacao;
	}

	public CargoDTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoDTO cargo) {
		this.cargo = cargo;
	}

	public List<MetasDTO> getMetasGerais() {
		return metasGerais;
	}

	public void setMetasGerais(List<MetasDTO> metasGerais) {
		this.metasGerais = metasGerais;
	}

	public List<MetasDTO> getMetasProjetos() {
		return metasProjetos;
	}

	public void setMetasProjetos(List<MetasDTO> metasProjetos) {
		this.metasProjetos = metasProjetos;
	}

	public List<MetasDTO> getMetasQuantitativas() {
		return metasQuantitativas;
	}

	public void setMetasQuantitativas(List<MetasDTO> metasQuantitativas) {
		this.metasQuantitativas = metasQuantitativas;
	}
}