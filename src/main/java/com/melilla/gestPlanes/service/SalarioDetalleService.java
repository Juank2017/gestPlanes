package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.SalarioDetalle;

public interface SalarioDetalleService {
	
	List<SalarioDetalle>obtenerDetalleSalario(long idSalario);
	
	List<SalarioDetalle>crearSalarioDetalle(SalarioDetalle salarioDetalle);
	
	List<SalarioDetalle>actualizarDetalleSalario(SalarioDetalle salarioDetalle);

}
