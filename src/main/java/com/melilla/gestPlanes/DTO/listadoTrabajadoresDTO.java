package com.melilla.gestPlanes.DTO;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class listadoTrabajadoresDTO {
	
	

	List<TrabajadoresDTO>trabajadores = new ArrayList<TrabajadoresDTO>();
	
	private long totalElements;
	
	
	
}
