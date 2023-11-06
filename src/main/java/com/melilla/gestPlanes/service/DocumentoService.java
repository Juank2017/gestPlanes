package com.melilla.gestPlanes.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.model.Documento;

public interface DocumentoService {
	
	
	public Documento guardarDocumento(Long idCiudadano,MultipartFile file,String tipo);
	
	public Resource loadDocumentAsResource(Long idCiudadano,String filename);
	
	public String eliminarDocumento(Long idCiudadano, String nombreDocumento);
	
	public Documento persistirBBDD(Documento documento);

}
