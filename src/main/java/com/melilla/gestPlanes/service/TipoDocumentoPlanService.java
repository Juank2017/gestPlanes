package com.melilla.gestPlanes.service;

import java.util.List;


import com.melilla.gestPlanes.model.TipoDocumentoPlan;

public interface TipoDocumentoPlanService {
	List<TipoDocumentoPlan>obtenerTipoDocumentos();
	
	
	
	boolean existeTipoDocumento(String tipoDocumento);
	
	TipoDocumentoPlan crearTipoDocumento(String tipoDocumento);
}
