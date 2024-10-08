package com.melilla.gestPlanes.service;

import com.melilla.gestPlanes.model.NominasReclamadas;

public interface NominaReclamadaService {
	
	NominasReclamadas insertarNomina(NominasReclamadas nomina);
	
	void eliminaNomina(long idNomina);
	
	NominasReclamadas editarNomina(NominasReclamadas nomina);
	
	NominasReclamadas getNomina(long idNomina);

}
