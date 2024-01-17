package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Presentacion;

public interface PresentacionService {
	
	Presentacion crearPresentacion(Presentacion presentacion);
	
	List<Presentacion> presentaciones();
	
	Presentacion actualizarPresentacion(Presentacion presentacion);
	
	void borrarPresentacion(Long idPresentacion);
	
	Presentacion buscarPresentacion(Long idPresentacion);

}
