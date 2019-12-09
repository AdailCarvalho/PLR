package br.com.zipext.plr.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.zipext.plr.dto.FolhaMetaMensalDTO;
import br.com.zipext.plr.model.FolhaMetaMensalModel;
import br.com.zipext.plr.model.MetasModel;
import br.com.zipext.plr.service.FolhaMetaMensalService;

@Controller
@RequestMapping("/folhasmensais")
public class FolhaMetaMensalController {

	@Autowired
	private FolhaMetaMensalService service;
	
	@GetMapping("/meta/{idMeta}/periodoPLR/{periodoPLR}")
	public ResponseEntity<List<FolhaMetaMensalDTO>> findByMeta(
			@PathVariable("idMeta") Long idMeta, 
			@PathVariable("periodoPLR") Integer periodoPLR) {
		
		List<FolhaMetaMensalModel> models = this.service.findByMetaAndAno(new MetasModel(idMeta), periodoPLR);
		List<FolhaMetaMensalDTO> dtos = new ArrayList<>();;
		
		if (models != null && !models.isEmpty()) {
			dtos.add(new FolhaMetaMensalDTO("META", null, models, false));
			dtos.add(new FolhaMetaMensalDTO("REAL", null, models, false));
		}

		return new ResponseEntity<List<FolhaMetaMensalDTO>>(dtos, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<List<FolhaMetaMensalDTO>> save(@RequestBody List<FolhaMetaMensalDTO> dtos) {
		List<FolhaMetaMensalModel> models = this.service.saveAll(dtos.stream().map(dto -> dto.obterModel()).collect(Collectors.toList()));
		List<FolhaMetaMensalDTO> results= new ArrayList<>();
		if (models != null && !models.isEmpty()) {
			results.add(new FolhaMetaMensalDTO("META", null, models, false));
			results.add(new FolhaMetaMensalDTO("REAL", null, models, false));
		}
		
		return new ResponseEntity<>(results, HttpStatus.OK);
	}
}