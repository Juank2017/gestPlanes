package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.exceptions.exceptions.FicheroCandidatosUploadException;
import com.melilla.gestPlanes.model.FicheroCargaCandidatos;

public interface FicheroCargaCandidatosService {
	
	FicheroCargaCandidatos subirFichero(MultipartFile fichero) throws FicheroCandidatosUploadException;
	
	void borrarFichero (long idFichero);
	
	List<FicheroCargaCandidatos> obtenerListadoFicheros();
	
	Resource descargarFichero (long id);
	
	List<FicheroCargaCandidatos> procesaFichero(long idFichero);
	
	

}
