package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearSalarioDTO;
import com.melilla.gestPlanes.DTO.UpdateSalarioDTO;
import com.melilla.gestPlanes.model.Salario;

public interface SalarioService {
	
	List<Salario>obtenerSalarios();
	
	List<Salario>obtenerSalariosPlan(long idPlan);
	
	List<Salario>actualizaSalario(UpdateSalarioDTO salario);
	
	Salario crearSalario(CrearSalarioDTO salario);
	
	Salario seleccionarSalario(long idSalario);
	
	Salario obtenerSalarioActivo(long idPlan,boolean activo);
	
	Salario activarSalario(long idSalario,long idPlan);

}
