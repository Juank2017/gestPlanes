package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.GeneraAcuerdoDTO;

public interface DocumentosProcedimientoService {
	
	
	void generaAcuerdo(List<GeneraAcuerdoDTO>acuerdos);
	
	void generaAcuerdoWord(List<GeneraAcuerdoDTO>acuerdos);
	
	

}
