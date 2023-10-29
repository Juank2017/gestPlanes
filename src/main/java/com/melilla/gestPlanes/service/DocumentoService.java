package com.melilla.gestPlanes.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentoService {
	
	
	public String guardarDocumento(Long idCiudadano,MultipartFile file);
	
	public Resource loadDocumentAsResource(Long idCiudadano,String filename);
	
	public String eliminarDocumento(Long idCiudadano, String nombreDocumento);

}
