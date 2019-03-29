package br.com.zipext.plr.export.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.com.zipext.plr.enums.EnumTipoMeta;
import br.com.zipext.plr.enums.EnumXlsEspecificasCells;
import br.com.zipext.plr.enums.EnumXlsGeraisCells;
import br.com.zipext.plr.enums.EnumXlsIdCells;
import br.com.zipext.plr.enums.EnumXlsLogoCells;
import br.com.zipext.plr.enums.EnumXlsMensaisCells;
import br.com.zipext.plr.enums.EnumXlsMensaisSection;
import br.com.zipext.plr.enums.EnumXlsSection;
import br.com.zipext.plr.enums.EnumXlsSheets;
import br.com.zipext.plr.export.FileExport;
import br.com.zipext.plr.model.ColaboradorCargoModel;
import br.com.zipext.plr.model.ColaboradorMetaEspecificaModel;
import br.com.zipext.plr.model.ColaboradorMetaGeralModel;
import br.com.zipext.plr.model.ColaboradorModel;
import br.com.zipext.plr.model.HistoricoMetaEspecificaMensalModel;
import br.com.zipext.plr.model.HistoricoMetaEspecificaModel;
import br.com.zipext.plr.model.HistoricoModel;
import br.com.zipext.plr.model.MetaEspecificaMensalModel;
import br.com.zipext.plr.model.MetaGeralModel;
import br.com.zipext.plr.utils.PLRUtils;

public class XlsFileExport extends FileExport {
	
	private Workbook workbook;
	
	double sumPontuacaoTotal = 0;
	
	public XlsFileExport(String templatePath) {
		this.initWorkbook(templatePath);
	}
	
	public ByteArrayInputStream processXlsForColaborador(ColaboradorModel colaboradorModel) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		this.writeContent(colaboradorModel);
		
		this.workbook.write(out);
		
		closeFileInputStream();

		return
				new ByteArrayInputStream(out.toByteArray());
	}
	
	public Workbook initWorkbook(String templatePath) {
		try {
			this.workbook = WorkbookFactory.create(getFileInputStream(templatePath));
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		
		return
				this.workbook;
	}
	
	public void writeContent(ColaboradorModel colaborador) {
		Sheet metaSheet = this.workbook.getSheet(EnumXlsSheets.METAS.getNome());
		//Header
		Row header = metaSheet.getRow(EnumXlsSection.LOGO.getRowNum());
		
		//Identificação
		Row identRow = metaSheet.getRow(EnumXlsSection.ID.getRowNum());
		Row ebitdaRow = metaSheet.getRow(EnumXlsSection.IBTIDA.getRowNum());
		Row indivRow = metaSheet.getRow(EnumXlsSection.INDIVIDUAL.getRowNum());
		Row partRow = metaSheet.getRow(EnumXlsSection.PARTICIPACAO.getRowNum());
		Row perfRow = metaSheet.getRow(EnumXlsSection.PERFORMANCE.getRowNum());
		Row metaExtraRow = metaSheet.getRow(EnumXlsSection.EXTRA.getRowNum());
		
		Cell nome = identRow.getCell(EnumXlsIdCells.NOME.getColIndex());
		Cell matricula = identRow.getCell(EnumXlsIdCells.MATRICULA.getColIndex());
		Cell cargo = identRow.getCell(EnumXlsIdCells.CARGO.getColIndex());
		Cell diretoria = identRow.getCell(EnumXlsIdCells.DIRETORIA.getColIndex());
		
		ColaboradorCargoModel carg = (ColaboradorCargoModel) colaborador.getColaboradoresCargos().toArray()[0];
		
		nome.setCellValue(colaborador.getNome());
		matricula.setCellValue(colaborador.getMatricula());
		cargo.setCellValue(carg.getPk().getCargo().getNome());
		diretoria.setCellValue(carg.getPk().getCargo().getDiretoria().getNome());
		
		Set<ColaboradorMetaGeralModel> metasGerais = colaborador.getColaboradoresMetasGerais();
		if (metasGerais != null && !metasGerais.isEmpty()) {
			for (ColaboradorMetaGeralModel metaGeral : metasGerais) {
				Cell val = null;
				Cell bonus = null;
				Cell observacao = null;
				MetaGeralModel m = metaGeral.getPk().getMetaGeral();
				switch (m.getId().intValue()) {
				case 1:
					val = ebitdaRow.getCell(EnumXlsGeraisCells.VAL_EBITDA.getColIndex());
					bonus = ebitdaRow.getCell(EnumXlsGeraisCells.BON_EBITDA.getColIndex());
					observacao = ebitdaRow.getCell(EnumXlsGeraisCells.OBS_EBITDA.getColIndex());
					
					val.setCellValue(metaGeral.getValor() != null ? metaGeral.getValor().doubleValue() : 0);
					
					break;
				case 2:
					bonus = indivRow.getCell(EnumXlsGeraisCells.BON_INDIV.getColIndex());
					observacao = indivRow.getCell(EnumXlsGeraisCells.OBS_INDIV.getColIndex());
					
					break;
				case 3:
					bonus = partRow.getCell(EnumXlsGeraisCells.BON_PARTIC.getColIndex());
					observacao = partRow.getCell(EnumXlsGeraisCells.OBS_PARTIC.getColIndex());
					
					break;
				case 4:
					bonus = perfRow.getCell(EnumXlsGeraisCells.BON_PERFOR.getColIndex());
					observacao = perfRow.getCell(EnumXlsGeraisCells.OBS_PERFOR.getColIndex());
					
					break;
				case 5:
					bonus = metaExtraRow.getCell(EnumXlsGeraisCells.BON_EXTRA.getColIndex());
					observacao = metaExtraRow.getCell(EnumXlsGeraisCells.OBS_EXTRA.getColIndex());
					
					break;
				default:
					break;
				}
				
				bonus.setCellValue(metaGeral.getBonus() != null ? (metaGeral.getBonus().doubleValue() / 100) : 0);
				observacao.setCellValue(metaGeral.getObservacao() != null ? metaGeral.getObservacao() : "N/I");
			}
			
			HistoricoModel historico = colaborador.getHistoricoExport();
			if (historico == null) {
				this.processMetasEspecificas(colaborador, metaSheet);
			} else {
				this.processMetasEspecificasFromHistorico(historico, metaSheet);
				header.getCell(EnumXlsLogoCells.NUM_DOC.getIndex()).setCellValue("Nº: " + historico.getId());
			}
			
			metaSheet.getRow(EnumXlsSection.PONTUACAO.getRowNum()).getCell(EnumXlsEspecificasCells.PONTUACAO.getColIndex())
																  .setCellValue(this.sumPontuacaoTotal / 100);
			
			this.workbook.setForceFormulaRecalculation(true);
		}
	}
	
	public void processMetasEspecificasFromHistorico(HistoricoModel historico, Sheet metaSheet) {
		List<HistoricoMetaEspecificaModel> quantitativas = historico.getHistoricoMetaEspecifica()
				.stream()
				.filter(m -> m.getPk().getColaboradorMetaEspecifica().getIdMeta().equals(1L))
				.sorted((m1, m2) -> m1.getPk().getColaboradorMetaEspecifica().getSequencia().compareTo(m2.getPk().getColaboradorMetaEspecifica().getSequencia()))
				.collect(Collectors.toList());
		
		List<HistoricoMetaEspecificaModel> projetos = historico.getHistoricoMetaEspecifica().stream()
				.filter(m -> m.getPk().getColaboradorMetaEspecifica().getIdMeta().equals(2L))
				.sorted((m1, m2) -> m1.getPk().getColaboradorMetaEspecifica().getSequencia().compareTo(m2.getPk().getColaboradorMetaEspecifica().getSequencia()))
				.collect(Collectors.toList());
		
		if (!quantitativas.isEmpty()) {
			this.fillMetasEspecificasFromHistorico(quantitativas, EnumXlsSection.QUANTITATIVAS, metaSheet);
		}
		
		if (!projetos.isEmpty()) {
			this.fillMetasEspecificasFromHistorico(projetos, EnumXlsSection.PROJETOS, metaSheet);
		}
	}
	
	public void processMetasEspecificas(ColaboradorModel colaborador, Sheet metaSheet) {
		List<ColaboradorMetaEspecificaModel> quantitativas = colaborador.getColaboradoresMetasEspecificas()
				.stream()
				.filter(m -> m.getIdMeta().equals(1L))
				.sorted((m1, m2) -> m1.getSequencia().compareTo(m2.getSequencia()))
				.collect(Collectors.toList());
		
		List<ColaboradorMetaEspecificaModel> projetos = colaborador.getColaboradoresMetasEspecificas().stream()
				.filter(m -> m.getIdMeta().equals(2L))
				.sorted((m1, m2) -> m1.getSequencia().compareTo(m2.getSequencia()))
				.collect(Collectors.toList());
		
		if (!quantitativas.isEmpty()) {
			this.fillMetasEspecificas(quantitativas, EnumXlsSection.QUANTITATIVAS, metaSheet);
		}
		
		if (!projetos.isEmpty()) {
			this.fillMetasEspecificas(projetos, EnumXlsSection.PROJETOS, metaSheet);
		}
	}
	
	private void fillMetasEspecificas(List<ColaboradorMetaEspecificaModel> itens, EnumXlsSection section, Sheet sheet) {
		int rowNum = section.getRowNum() + 2;
		int metaIndex = 1;
		double sumPesos = 0;
		Cell sumPesosCell = sheet.getRow(rowNum).getCell(EnumXlsEspecificasCells.SUMPESOS.getColIndex());
		for (ColaboradorMetaEspecificaModel item: itens) {
			Row row = sheet.getRow(rowNum);
			
			row.getCell(EnumXlsEspecificasCells.SEQUENCIA.getColIndex()).setCellValue(item.getSequencia());
			row.getCell(EnumXlsEspecificasCells.DESCRICAO.getColIndex()).setCellValue(item.getDescricao() != null ? item.getDescricao() : "");
			row.getCell(EnumXlsEspecificasCells.FREQUENCIA.getColIndex()).setCellValue(item.getFrequenciaMedicao());
			row.getCell(EnumXlsEspecificasCells.PESOS.getColIndex()).setCellValue(item.getPeso().doubleValue() / 100);
			row.getCell(EnumXlsEspecificasCells.META.getColIndex()).setCellValue(item.getMeta() != null ? item.getMeta() : "");
			row.getCell(EnumXlsEspecificasCells.OBSERVACOES.getColIndex()).setCellValue(item.getObservacao() != null ? item.getObservacao() : "");
			row.getCell(EnumXlsEspecificasCells.PRAZOS.getColIndex()).setCellValue(item.getPrazo().format(DateTimeFormatter.ofPattern(PLRUtils.DATE_PATTERN_JS)));
			
			if (!item.getMetasMensais().isEmpty()) {
				this.fillMetasEspecificasMensais(item, metaIndex);
			}
			
			sumPesos += item.getPeso().doubleValue();
			rowNum++;
			metaIndex++;
			
		}
		
		sumPesosCell.setCellValue(sumPesos / 100);
		
		this.sumPontuacaoTotal += sumPesos;
	}
	
	private void fillMetasEspecificasFromHistorico(List<HistoricoMetaEspecificaModel> itens, EnumXlsSection section, Sheet sheet) {
		int rowNum = section.getRowNum() + 2;
		int metaIndex = 1;
		double sumPesos = 0;
		Cell sumPesosCell = sheet.getRow(rowNum).getCell(EnumXlsEspecificasCells.SUMPESOS.getColIndex());
		for (HistoricoMetaEspecificaModel item: itens) {
			Row row = sheet.getRow(rowNum);
			
			row.getCell(EnumXlsEspecificasCells.SEQUENCIA.getColIndex()).setCellValue(item.getPk().getColaboradorMetaEspecifica().getSequencia());
			row.getCell(EnumXlsEspecificasCells.DESCRICAO.getColIndex()).setCellValue(item.getDescricao() != null ? item.getDescricao() : "");
			row.getCell(EnumXlsEspecificasCells.FREQUENCIA.getColIndex()).setCellValue(item.getFrequenciaMedicao());
			row.getCell(EnumXlsEspecificasCells.PESOS.getColIndex()).setCellValue(item.getPeso().doubleValue() / 100);
			row.getCell(EnumXlsEspecificasCells.META.getColIndex()).setCellValue(item.getMeta() != null ? item.getMeta() : "");
			row.getCell(EnumXlsEspecificasCells.OBSERVACOES.getColIndex()).setCellValue(item.getObservacao() != null ? item.getObservacao() : "");
			row.getCell(EnumXlsEspecificasCells.PRAZOS.getColIndex()).setCellValue(item.getPrazo().format(DateTimeFormatter.ofPattern(PLRUtils.DATE_PATTERN_JS)));
			
			if (!item.getPk().getHistorico().getHistoricoMetaEspecificaMensal().isEmpty()) {
				this.fillMetasEspecificasMensaisFromHistorico(item, metaIndex);
			}
			
			sumPesos += item.getPeso().doubleValue();
			rowNum++;
			metaIndex++;
		}
		
		sumPesosCell.setCellValue(sumPesos / 100);
		
		this.sumPontuacaoTotal += sumPesos;
	}
	
	private void fillMetasEspecificasMensais(ColaboradorMetaEspecificaModel item, int metaIndex) {
		Sheet sheet = this.workbook.getSheet(String.valueOf(EnumTipoMeta.forId(item.getIdMeta().intValue()).getAbv()).concat(String.valueOf(metaIndex)));
		
		Row resumoMeta = sheet.getRow(EnumXlsMensaisSection.RESUMO_META.getRowNum());
		Row planejadasRow = sheet.getRow(EnumXlsMensaisSection.METAS_PLANEJADAS.getRowNum());
		Row acumuladasRow = sheet.getRow(EnumXlsMensaisSection.METAS_REALIZADAS.getRowNum());
		Row aggPlanejadasRow = sheet.getRow(EnumXlsMensaisSection.AGG_PLANEJADAS.getRowNum());
		Row aggRealizadasRow = sheet.getRow(EnumXlsMensaisSection.AGG_REALIZADAS.getRowNum());
		
		resumoMeta.getCell(EnumXlsMensaisCells.SEQUENCIA_META.getColIndex()).setCellValue("Meta : " + metaIndex);
		resumoMeta.getCell(EnumXlsMensaisCells.DESCRICAO_META.getColIndex()).setCellValue(item.getDescricao());
		
		List<MetaEspecificaMensalModel> metasMensais = item.getMetasMensais();
		
		for (MetaEspecificaMensalModel itm : metasMensais) {
			double meta = itm.getValorMeta() != null ? itm.getValorMeta().doubleValue() : 0;
			double realizado = itm.getValorRealizado() != null ? itm.getValorRealizado().doubleValue() : 0;
			
			planejadasRow.getCell(EnumXlsMensaisCells.forMes(itm.getNumMes()).getColIndex()).setCellValue(meta);
			acumuladasRow.getCell(EnumXlsMensaisCells.forMes(itm.getNumMes()).getColIndex()).setCellValue(realizado);
		}
		
		double avgMensalPlan = metasMensais.stream().mapToDouble(m -> m.getValorMetaAsDouble()).average().orElse(0);
		double avgMensalRealizado = metasMensais.stream().mapToDouble(m -> m.getValorRealizadoAsDouble()).average().orElse(0);
		double sumMensalPlan = metasMensais.stream().mapToDouble(m -> m.getValorMetaAsDouble()).sum();
		double sumMensalRealizado = metasMensais.stream().mapToDouble(m -> m.getValorRealizadoAsDouble()).sum();
		
		aggPlanejadasRow.getCell(EnumXlsMensaisCells.AGG_SUM.getColIndex()).setCellValue(sumMensalPlan);
		aggPlanejadasRow.getCell(EnumXlsMensaisCells.AGG_AVG.getColIndex()).setCellValue(avgMensalPlan);
		aggRealizadasRow.getCell(EnumXlsMensaisCells.AGG_SUM.getColIndex()).setCellValue(sumMensalRealizado);
		aggRealizadasRow.getCell(EnumXlsMensaisCells.AGG_AVG.getColIndex()).setCellValue(avgMensalRealizado);
	}
	
	private void fillMetasEspecificasMensaisFromHistorico(HistoricoMetaEspecificaModel item, int metaIndex) {
		Sheet sheet = this.workbook.getSheet(String.valueOf(EnumTipoMeta
				.forId(item.getPk().getColaboradorMetaEspecifica().getIdMeta().intValue())
				.getAbv())
				.concat(String.valueOf(metaIndex)));
		
		Row resumoMeta = sheet.getRow(EnumXlsMensaisSection.RESUMO_META.getRowNum());
		Row planejadasRow = sheet.getRow(EnumXlsMensaisSection.METAS_PLANEJADAS.getRowNum());
		Row acumuladasRow = sheet.getRow(EnumXlsMensaisSection.METAS_REALIZADAS.getRowNum());
		Row aggPlanejadasRow = sheet.getRow(EnumXlsMensaisSection.AGG_PLANEJADAS.getRowNum());
		Row aggRealizadasRow = sheet.getRow(EnumXlsMensaisSection.AGG_REALIZADAS.getRowNum());
		
		resumoMeta.getCell(EnumXlsMensaisCells.SEQUENCIA_META.getColIndex()).setCellValue("Meta : " + metaIndex);
		resumoMeta.getCell(EnumXlsMensaisCells.DESCRICAO_META.getColIndex()).setCellValue(item.getDescricao());
		
		List<HistoricoMetaEspecificaMensalModel> metasMensais = item.getPk().getHistorico().getHistoricoMetaEspecificaMensal()
				.stream().filter(p -> p.getPk().getMetaEspecificaMensal().getPk().getColaboradorMetaEspecifica().equals(item.getPk().getColaboradorMetaEspecifica()))
				.collect(Collectors.toList());
		
		for (HistoricoMetaEspecificaMensalModel itm : metasMensais) {
			double meta = itm.getValorMeta() != null ? itm.getValorMeta().doubleValue() : 0;
			double realizado = itm.getValorRealizado() != null ? itm.getValorRealizado().doubleValue() : 0;
			
			planejadasRow.getCell(EnumXlsMensaisCells.forMes(itm.getNumMes()).getColIndex()).setCellValue(meta);
			acumuladasRow.getCell(EnumXlsMensaisCells.forMes(itm.getNumMes()).getColIndex()).setCellValue(realizado);
		}
		
		double avgMensalPlan = metasMensais.stream().mapToDouble(m -> m.getValorMetaAsDouble()).average().orElse(0);
		double avgMensalRealizado = metasMensais.stream().mapToDouble(m -> m.getValorRealizadoAsDouble()).average().orElse(0);
		double sumMensalPlan = metasMensais.stream().mapToDouble(m -> m.getValorMetaAsDouble()).sum();
		double sumMensalRealizado = metasMensais.stream().mapToDouble(m -> m.getValorRealizadoAsDouble()).sum();
		
		aggPlanejadasRow.getCell(EnumXlsMensaisCells.AGG_SUM.getColIndex()).setCellValue(sumMensalPlan);
		aggPlanejadasRow.getCell(EnumXlsMensaisCells.AGG_AVG.getColIndex()).setCellValue(avgMensalPlan);
		aggRealizadasRow.getCell(EnumXlsMensaisCells.AGG_SUM.getColIndex()).setCellValue(sumMensalRealizado);
		aggRealizadasRow.getCell(EnumXlsMensaisCells.AGG_AVG.getColIndex()).setCellValue(avgMensalRealizado);
	}
}
