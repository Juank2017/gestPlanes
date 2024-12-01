package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.SalarioDetalle;
import com.melilla.gestPlanes.repository.SalarioDetalleRepository;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioDetalleService;

public class SalarioDetalleServiceImpl  implements SalarioDetalleService{

	@Autowired
	private SalarioDetalleRepository salarioDetalleRepository;
	
	@Autowired
	private SalarioRepository salarioRepository;
	
	@Override
	public List<SalarioDetalle> obtenerDetalleSalario(long idSalario) {
		
		return salarioDetalleRepository.findByIdSalarioDetalle(idSalario);
	}

	@Override
	public List<SalarioDetalle> actualizarDetalleSalario(SalarioDetalle salarioDetalle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalarioDetalle> crearSalarioDetalle(SalarioDetalle salarioDetalle) {
		
		Salario salario = salarioRepository.findById(salarioDetalle.getSalario().getIdSalario()).orElseThrow(()->new SalarioNotFoundException(salarioDetalle.getSalario().getIdSalario()));
		
		salario.getDetalles().add(salarioDetalleRepository.save(salarioDetalle));
		
		return obtenerDetalleSalario(salario.getIdSalario());
	}
	
	

}
