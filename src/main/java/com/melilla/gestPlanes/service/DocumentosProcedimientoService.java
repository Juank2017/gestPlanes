package com.melilla.gestPlanes.service;

import java.util.List;


import com.melilla.gestPlanes.DTO.DocumentoProcedimientoAZip;
import com.melilla.gestPlanes.DTO.GeneraAcuerdoDTO;
import com.melilla.gestPlanes.model.DocumentoProcedimientoReclamacion;

import jakarta.servlet.http.HttpServletResponse;

public interface DocumentosProcedimientoService {
	
	
	void generaAcuerdo(List<GeneraAcuerdoDTO>acuerdos);
	
	List<DocumentoProcedimientoAZip> generaAcuerdoWord(List<GeneraAcuerdoDTO>acuerdos);
	
	public void downloadDocumentsAsZipFile(HttpServletResponse response, List<DocumentoProcedimientoAZip> docs);
	
	void eliminaDocumentoProcedimiento(long idDocumento);

}
