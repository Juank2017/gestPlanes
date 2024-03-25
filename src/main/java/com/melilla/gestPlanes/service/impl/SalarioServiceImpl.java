package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
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


	@Override
	public List<Salario> actualizaSalario(Salario salario) {
		Salario salarioBBDD = salarioRepository.findById(salario.getGrupo()).orElseThrow(()->new SalarioNotFoundException(salario.getGrupo()));
		
		salarioBBDD.setBase(salario.getBase());
		salarioBBDD.setProrrata(salario.getProrrata());
		salarioBBDD.setResidencia(salario.getResidencia());
		salarioBBDD.setTotal(salario.getTotal());
		
		salarioRepository.save(salarioBBDD);
		
		
		return salarioRepository.findAllByPlanIdPlan(salarioBBDD.getPlan().getIdPlan());
	}
	
	

}
