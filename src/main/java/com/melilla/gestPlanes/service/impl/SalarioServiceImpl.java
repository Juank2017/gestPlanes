package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarioServiceImpl implements SalarioService {
	
	@Autowired
	SalarioRepository salarioRepository;
	
	
	@Override
	public List<Salario> obtenerSalarios(Long idPlan) {
		
		return salarioRepository.findAllByPlanIdPlan(idPlan);
	}
	
	

}
