package br.com.zipext.plr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zipext.plr.model.FormulaModel;

@Repository
public interface FormulaRepository extends JpaRepository<FormulaModel, Long> {

	public List<FormulaModel> findAllByOrderByNomeAsc();
}

