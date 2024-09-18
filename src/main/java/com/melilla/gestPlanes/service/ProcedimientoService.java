package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.ProcedimientoDTO;
import com.melilla.gestPlanes.model.Procedimiento;


public interface ProcedimientoService {

	List<ProcedimientoDTO>obtenerProcedimientos();
	
	List<Procedimiento>procedimientos();
}
