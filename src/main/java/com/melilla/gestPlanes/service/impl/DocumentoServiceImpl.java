package com.melilla.gestPlanes.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.DocumentoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class DocumentoServiceImpl implements DocumentoService {
	
	@Autowired
	private CiudadanoService ciudadanoService;
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public String guardarDocumento(Long idCiudadano, MultipartFile file) {
		//Obtiene el ciudadano
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano).orElseThrow(()-> new CiudadanoNotFoundException(idCiudadano));
		//ocupacion del ciudadano
		String ocupacion = ciudadano.getContrato().getOcupacion()+"\\";
		//forma el nombre de la capeta con apellidos_nombre
		String nombreCarpeta = ocupacion + ciudadano.getApellidos()+"_"+ciudadano.getNombre();
		//obtiene el path absoluto debe ser S:\PLANES DE EMPLEO\ocupacion\apellidos_nombre
		Path fileStorageLocation = Paths.get(uploadDir+nombreCarpeta).toAbsolutePath().normalize();
		log.info(fileStorageLocation.toString());
		//Intenta crear el directorio si no existe.
		try {
			Files.createDirectories(fileStorageLocation);
		}catch (Exception e){
			throw new FileStorageException("No se ha podido crear el directorio: "+fileStorageLocation);
		}
		
		//nombre del fichero
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("El nombre de archivo tiene una secuencia de carácteres no válida " + fileName);
            }
		 // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    } catch (IOException ex) {
        throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
    }
		
		
	}

	@Override
	public Resource loadDocumentAsResource(Long idCiudadano, String filename) {
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano).orElseThrow(()-> new CiudadanoNotFoundException(idCiudadano));
		
		String nombreCarpeta = ciudadano.getApellidos()+"_"+ciudadano.getNombre()+"\\";
		 try {
			 Path fileStorageLocation = Paths.get(uploadDir+nombreCarpeta+filename).toAbsolutePath().normalize();
			 log.info(fileStorageLocation.toString());
	            Resource resource = new UrlResource(fileStorageLocation.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new MyFileNotFoundException("File not found " + filename);
	            }
	        } catch (MalformedURLException ex) {
	            throw new MyFileNotFoundException("File not found " + filename);
	        }
	}

	@Override
	public String eliminarDocumento(Long idCiudadano, String nombreDocumento) {
		// TODO Auto-generated method stub
		return null;
	}

}
