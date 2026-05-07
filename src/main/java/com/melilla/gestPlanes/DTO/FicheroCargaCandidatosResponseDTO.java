package com.melilla.gestPlanes.DTO;

import java.util.ArrayList;
import java.util.List;

import com.melilla.gestPlanes.model.Ciudadano;

import lombok.Data;

@Data
public class FicheroCargaCandidatosResponseDTO {
	
	List<TrabajadoresDTO> candidatos = new ArrayList<TrabajadoresDTO>();
	
	List<CreateTrabajadorDTO> candidatosConError = new ArrayList<CreateTrabajadorDTO>();
	
	List<String> errores= new ArrayList<String>();

}
