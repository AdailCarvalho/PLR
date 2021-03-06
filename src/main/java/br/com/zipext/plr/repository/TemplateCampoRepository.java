package br.com.zipext.plr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zipext.plr.model.TemplateCampoModel;
import br.com.zipext.plr.model.TemplateModel;

@Repository
public interface TemplateCampoRepository extends JpaRepository<TemplateCampoModel, Long> {

	public List<TemplateCampoModel> findByTemplateAndArea(TemplateModel template, String area);
}
