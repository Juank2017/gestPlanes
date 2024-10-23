package com.melilla.gestPlanes.service;

import java.math.BigDecimal;
import java.util.List;

import com.melilla.gestPlanes.DTO.CreateProcedimientoDTO;
import com.melilla.gestPlanes.DTO.ProcedimientoDTO;
import com.melilla.gestPlanes.DTO.UpdatePeriodosDTO;
import com.melilla.gestPlanes.model.Procedimiento;


public interface ProcedimientoService {

	List<ProcedimientoDTO>obtenerProcedimientos();
	
	List<Procedimiento>procedimientos();
	
	Procedimiento crearProcedimiento(CreateProcedimientoDTO prodedimiento);
	
	Procedimiento getProcedimiento(Long idProcedimiento);
	
	Procedimiento updateContratoReclamadoProcedimiento(UpdatePeriodosDTO periodo);
		
	Procedimiento updateProcedimiento(ProcedimientoDTO procedimiento);
	
	BigDecimal totalReconocidoProcedimiento(long idProcedimiento);
		
	
}
