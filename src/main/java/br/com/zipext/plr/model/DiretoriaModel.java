package br.com.zipext.plr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "CORPORATIVO", name = "CAD_DIRETORIA")
public class DiretoriaModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cadDiretoriaSeq")
	@SequenceGenerator(schema = "CORPORATIVO", name = "cadDiretoriaSeq",  sequenceName = "cad_diretoria_seq", allocationSize = 1)
	@Column(name = "CD_DIRETORIA")
	private Long id;
	
	@Column(name = "NM_DIRETORIA")
	private String nome;

	public DiretoriaModel() {}
	
	public DiretoriaModel(Long id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiretoriaModel other = (DiretoriaModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
