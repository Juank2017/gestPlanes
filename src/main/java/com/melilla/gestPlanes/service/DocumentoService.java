package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.DTO.GeneraContratoDTO;
import com.melilla.gestPlanes.DTO.GeneraContratoResponseDTO;
import com.melilla.gestPlanes.model.Documento;

public interface DocumentoService {
	
	
	public Documento guardarDocumento(Long idCiudadano,MultipartFile file,String tipo);
	
	public Resource loadDocumentAsResource(Long idCiudadano,String filename,Long idDocumento);
	
	public String eliminarDocumento(Long idCiudadano, String nombreDocumento);
	
	public Documento guardarBBDD(Documento documento);
	
	List<GeneraContratoResponseDTO> generarContrato(List<GeneraContratoDTO> trabajadores);
	
	Documento obtenerDocumentoPorNombreIdCiudadano(String fileName,Long idCiduadano);

}
