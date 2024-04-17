package com.melilla.gestPlanes.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.DTO.DocumentoAZip;
import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.DTO.GeneraContratoDTO;
import com.melilla.gestPlanes.DTO.GeneraContratoResponseDTO;
import com.melilla.gestPlanes.DTO.GeneraPresentacionDTO;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.model.TipoDocumento;
import com.melilla.gestPlanes.model.TipoDocumentoPlan;

import jakarta.servlet.http.HttpServletResponse;

public interface DocumentoService {
	
	
	public Documento guardarDocumento(Long idCiudadano,MultipartFile file,String tipo);
	
	public DocumentoPlan guardarDocumentoPlan(Long idPlan,MultipartFile file,String tipo);
	
	public Resource loadDocumentAsResource(Long idCiudadano,String filename,Long idDocumento);
	public Resource loadDocumentPlanAsResource( String filename, Long idDocumento);
	
	public void eliminarDocumento(Long idDocumento);
	public void eliminarDocumentoPlan(Long idDocumento);
	
	public Documento guardarBBDD(Documento documento);
	
	List<GeneraContratoResponseDTO> generarContrato(List<GeneraContratoDTO> trabajadores);
	
	List<GeneraContratoResponseDTO> generarPresentacion(List<GeneraPresentacionDTO> trabajadores);
	
	Documento obtenerDocumentoPorNombreIdCiudadano(String fileName,Long idCiduadano);
	
	void downloadDocumentsAsZipFile(HttpServletResponse response,List<DocumentoAZip> docs);
	public void downloadDocumentsPlanAsZipFile(HttpServletResponse response, List<DocumentoAZip> docs);

	List<GeneraContratoResponseDTO> buscarDocumentos(List<DocumentoCriterioBusqueda> criterios);
	List<DocumentoPlan> buscarDocumentosPlan(List<DocumentoCriterioBusqueda> criterios);
	
	List<TipoDocumento> tipoDocumentos();
	List<TipoDocumentoPlan> tipoDocumentosPlan();
	
	List<Documento>obtenerDocumentosTrabajador(Long idCiudadano);
}
