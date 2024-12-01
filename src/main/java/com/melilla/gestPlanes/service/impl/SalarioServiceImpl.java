package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.SalarioDetalle;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarioServiceImpl implements SalarioService {
	
	@Autowired
	SalarioRepository salarioRepository;
	
	
	@Override
	public List<Salario> obtenerSalarios() {
		
		
		
		
		return salarioRepository.findAll();
	}


	@Override
	public List<Salario> actualizaSalario(Salario salario) {
		Salario salarioBBDD = salarioRepository.findById(salario.getIdSalario()).orElseThrow(()->new SalarioNotFoundException(salario.getIdSalario()));
		
		salarioBBDD.setDescripcion(salario.getDescripcion());
		salarioBBDD.setDetalles(salario.getDetalles());
		salarioBBDD.setPlan(salario.getPlan());
		
		salarioRepository.save(salarioBBDD);
		
		
		return salarioRepository.findAllByPlanIdPlan(salarioBBDD.getPlan().getIdPlan());
	}


	@Override
	public List<Salario> obtenerSalariosPlan(long idPlan) {
		
		return salarioRepository.findAllByPlanIdPlan(idPlan);
	}
	
	

}
