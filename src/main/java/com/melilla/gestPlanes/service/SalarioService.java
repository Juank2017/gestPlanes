package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Salario;

public interface SalarioService {
	
	List<Salario>obtenerSalarios(Long idPlan);

}
