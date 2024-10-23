package com.melilla.gestPlanes.service;

import java.math.BigDecimal;

import com.melilla.gestPlanes.DTO.TotalesNominaReclamada;
import com.melilla.gestPlanes.model.NominasReclamadas;

public interface NominaReclamadaService {
	
	NominasReclamadas insertarNomina(NominasReclamadas nomina);
	
	void eliminaNomina(long idNomina);
	
	NominasReclamadas editarNomina(NominasReclamadas nomina);
	
	NominasReclamadas getNomina(long idNomina);
	
	TotalesNominaReclamada totalDevengadoNomina(NominasReclamadas nomina);

}
