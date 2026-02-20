package com.melilla.gestPlanes.DTO;

import java.util.List;

import com.melilla.gestPlanes.model.ParteBaja;

public interface ListadoTrabajadoresConPartes {
	
	long getIdCiudadano();
	
	String getDNI();
	String getNombre();
	String getApellido1();
	String getApellido2();
	List<ParteBaja>getPartes();

}
