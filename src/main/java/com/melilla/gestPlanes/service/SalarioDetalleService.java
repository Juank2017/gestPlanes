package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.ResponseSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.UpdateDetalleSalarioDTO;
import com.melilla.gestPlanes.model.SalarioDetalle;

public interface SalarioDetalleService {
	
	List<ResponseSalarioDetalleDTO>obtenerDetalleSalario(long idSalario);
	
	List<ResponseSalarioDetalleDTO>obtenerDetalleSalarioActivo(long idSalario);
	
	SalarioDetalle crearSalarioDetalle(CrearSalarioDetalleDTO salarioDetalle);
	
	SalarioDetalle actualizarDetalleSalario(UpdateDetalleSalarioDTO salarioDetalle);
	
	List<ResponseSalarioDetalleDTO> borraDetalleSalario(long idSalarioDetalle);

}
