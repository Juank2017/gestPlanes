package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearSalarioDTO;
import com.melilla.gestPlanes.DTO.UpdateSalarioDTO;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.SalarioDetalle;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarioServiceImpl implements SalarioService {
	
	@Autowired
	SalarioRepository salarioRepository;
	
	@Autowired
	PlanRepository planRepository;
	
	
	@Override
	public List<Salario> obtenerSalarios() {
		
		
		
		
		return salarioRepository.findAll();
	}


	@Override
	public List<Salario> actualizaSalario(UpdateSalarioDTO salario) {
		Salario salarioBBDD = salarioRepository.findById(salario.getIdSalario()).orElseThrow(()->new SalarioNotFoundException(salario.getIdSalario()));
		
		salarioBBDD.setDescripcion(salario.getDescripcion());
		
		
		
		salarioBBDD.setPlan(planRepository.findById(salario.getIdPlan()).orElseThrow(()->new PlanNotFoundException(salario.getIdPlan()+"")));
		
		salarioRepository.save(salarioBBDD);
		
		
		return salarioRepository.findAllByPlanIdPlan(salarioBBDD.getPlan().getIdPlan());
	}


	@Override
	public List<Salario> obtenerSalariosPlan(long idPlan) {
		
		return salarioRepository.findAllByPlanIdPlan(idPlan);
	}


	@Override
	public Salario crearSalario(CrearSalarioDTO salario) {
		
		Salario nuevo = new Salario();
		
		nuevo.setDescripcion(salario.getDescripcion());
		nuevo.setPlan(planRepository.findById(salario.getIdPlan()).orElseThrow(()->new PlanNotFoundException(salario.getIdPlan()+"")));
		nuevo.setActivo(false);
		nuevo = salarioRepository.save(nuevo);
		seleccionarSalario(nuevo.getIdSalario());
		return nuevo;
	}


	@Override
	public Salario seleccionarSalario(long idSalario) {
		
		List<Salario>salarios = salarioRepository.findAll();
		
		Salario salarioSeleccionado = salarioRepository.findById(idSalario).orElseThrow(()->new SalarioNotFoundException(idSalario));
		
		salarios.forEach((s)->{
			
			s.setActivo(false);
			salarioRepository.save(s);
		});
		
		salarioSeleccionado.setActivo(true);
		
			
		return salarioRepository.save(salarioSeleccionado); 
	}


	@Override
	public Salario obtenerSalarioActivo(long idPlan, boolean activo) {
		
		
		
		
		return salarioRepository.findByPlanIdPlanAndActivo(idPlan, activo);
	}


	@Override
	public Salario activarSalario(long idSalario,long idPlan) {
		
		Salario salarioParaActivar = salarioRepository.findByIdSalarioAndPlanIdPlan(idSalario,idPlan).orElseThrow(()-> new SalarioNotFoundException(idSalario));
		List<Salario> salarios = salarioRepository.findAllByPlanIdPlan(idPlan);
		for (Salario salario : salarios) {
			salario.setActivo(false);
			salarioRepository.save(salario);
		}
		
		salarioParaActivar.setActivo(true);
		
		return salarioRepository.save(salarioParaActivar);
		
		
		
		
	}
	
	

}
