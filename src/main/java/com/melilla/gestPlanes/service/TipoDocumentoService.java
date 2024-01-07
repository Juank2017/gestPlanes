package com.melilla.gestPlanes.service;

import java.util.List;



import com.melilla.gestPlanes.model.TipoDocumento;


public interface TipoDocumentoService {
	List<TipoDocumento>obtenerTipoDocumentos();
	
	boolean existeTipoDocumento(String tipoDocumento);
	
	TipoDocumento crearTipoDocumento(String tipoDocumento);
}
