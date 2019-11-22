package br.com.zipext.plr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zipext.plr.model.FrequenciaMedicaoModel;

@Repository
public interface FrequenciaMedicaoRepository extends JpaRepository<FrequenciaMedicaoModel, Long> {

	public List<FrequenciaMedicaoModel> findAllByOrderByDescricaoAsc();
}
