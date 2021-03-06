package br.com.zipext.plr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.zipext.plr.model.FolhaMetaItemModel;
import br.com.zipext.plr.model.FolhaMetaMensalModel;
import br.com.zipext.plr.model.MetasModel;
import br.com.zipext.plr.repository.FolhaMetaMensalRepository;

@Service
public class FolhaMetaMensalService {

	@Autowired
	private FolhaMetaMensalRepository repository;
	
	@Transactional(readOnly = true)
	public Long countByMeta(MetasModel meta) {
		return this.repository.countByMeta(meta);
	}
	
	@Modifying
	@Transactional(readOnly = false)
	public void deleteAll(List<FolhaMetaMensalModel> models) {
		this.repository.deleteAll(models);
	}
	
	@Transactional(readOnly = false)
	public void deleteByMeta(MetasModel meta) {
		this.repository.deleteByMeta(meta);
	}
	
	@Transactional(readOnly = true)
	public List<FolhaMetaMensalModel> findAll() {
		return
				this.repository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<FolhaMetaMensalModel> findByMetaAndAno(MetasModel meta, Integer ano) {
		return
				this.repository.findByMetaAndAno(meta, ano);
	}
	
	@Transactional(readOnly = true)
	public List<FolhaMetaMensalModel> findByFolhaMetaItem(FolhaMetaItemModel folhaMetaItem) {
		List<FolhaMetaMensalModel> models = 
				this.findByMetaAndAno(folhaMetaItem.getMeta(), folhaMetaItem.getFolhaMeta().getInicioVigencia().getAno());
		
		return
				models;
	}
	
	@Transactional(readOnly = false)
	public List<FolhaMetaMensalModel> saveAll(List<FolhaMetaMensalModel> models) {
		return this.repository.saveAll(models);
	}
}
