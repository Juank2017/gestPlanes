package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.ResponseSalarioDetalleDTO;
import com.melilla.gestPlanes.model.SalarioDetalle;

public interface SalarioDetalleService {
	
	List<ResponseSalarioDetalleDTO>obtenerDetalleSalario(long idSalario);
	
	SalarioDetalle crearSalarioDetalle(CrearSalarioDetalleDTO salarioDetalle);
	
	List<SalarioDetalle>actualizarDetalleSalario(SalarioDetalle salarioDetalle);

}
