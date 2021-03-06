package br.com.zipext.plr.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import br.com.zipext.plr.model.PerfilModel;
import br.com.zipext.plr.model.PerfilPermissaoModel;
import br.com.zipext.plr.model.PermissaoModel;

public class PerfilDTO {

	private Long id;
	
	private String nome;
	
	private Character situacao;
	
	private List<Long> idsAreaPermissaoAcesso;
	
	private boolean isEditable;
	
	public PerfilDTO() {
		this.isEditable = false;
	}
	
	public PerfilDTO(PerfilModel model) {
		BeanUtils.copyProperties(model, this);
	}
	
	public PerfilDTO(PerfilPermissaoModel model) {
		PerfilModel perfil = model.getPk().getPerfil();
		PermissaoModel permissao = model.getPk().getPermissao();
		
		BeanUtils.copyProperties(perfil, this);
		
		if (permissao.getPermissao().equals('W')) {
			this.isEditable = true;
		} else {
			this.isEditable = false;
		}
		
		this.idsAreaPermissaoAcesso = perfil.getPerfisAcesso().stream().map(pa -> pa.getPk().getInformacaoAcesso().getId()).collect(Collectors.toList());
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
	
	public Character getSituacao() {
		return situacao;
	}

	public void setSituacao(Character situacao) {
		this.situacao = situacao;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<Long> getIdsAreaPermissaoAcesso() {
		return idsAreaPermissaoAcesso;
	}

	public void setIdsAreaPermissaoAcesso(List<Long> idsAreaPermissaoAcesso) {
		this.idsAreaPermissaoAcesso = idsAreaPermissaoAcesso;
	}
}
