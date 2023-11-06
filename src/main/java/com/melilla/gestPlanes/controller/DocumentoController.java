package com.melilla.gestPlanes.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoResponse;
import com.melilla.gestPlanes.service.DocumentoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class DocumentoController {

	@Autowired
	private DocumentoService documentoService;
	
	
	@PostMapping("/subirDocumento")
	public ResponseEntity<ApiResponse> subirDocumento(@RequestPart MultipartFile file,@RequestPart String tipo, @RequestPart String idCiudadano) {
		
		ApiResponse response = new ApiResponse();
		
		Documento doc = documentoService.guardarDocumento(Long.parseLong(idCiudadano), file,tipo);
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(documentoService.persistirBBDD(doc));
		response.setMensaje("Lista de ciudadanos");
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/descargaDocumento/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = documentoService.loadDocumentAsResource(1l,fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        	throw new FileStorageException("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	
}