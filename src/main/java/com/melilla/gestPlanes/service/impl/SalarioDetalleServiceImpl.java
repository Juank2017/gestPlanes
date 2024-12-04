package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.ResponseSalarioDetalleDTO;
import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.SalarioDetalle;
import com.melilla.gestPlanes.repository.SalarioDetalleRepository;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioDetalleService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SalarioDetalleServiceImpl  implements SalarioDetalleService{

	@Autowired
	private SalarioDetalleRepository salarioDetalleRepository;
	
	@Autowired
	private SalarioRepository salarioRepository;
	
	@Override
	public List<ResponseSalarioDetalleDTO> obtenerDetalleSalario(long idSalario) {
		
		List<SalarioDetalle>listado =salarioDetalleRepository.findAllBySalarioIdSalario(idSalario);
		
		List<ResponseSalarioDetalleDTO> salida = new ArrayList<ResponseSalarioDetalleDTO>();
		
		for (SalarioDetalle salarioDetalle : listado) {
			
			ResponseSalarioDetalleDTO response = new ResponseSalarioDetalleDTO();
			
			response.setIdSalarioDetalle(salarioDetalle.getIdSalarioDetalle()+"");
			response.setGrupo(salarioDetalle.getGrupo()+"");
			response.setBase(salarioDetalle.getBase());
			response.setProrrata(salarioDetalle.getProrrata());
			response.setResidencia(salarioDetalle.getResidencia());
			response.setTotal(salarioDetalle.getTotal());
			salida.add(response);
			
		}
		
		
		return salida;
	}

	@Override
	public List<SalarioDetalle> actualizarDetalleSalario(SalarioDetalle salarioDetalle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SalarioDetalle crearSalarioDetalle(CrearSalarioDetalleDTO salarioDetalle) {
		
		Salario salario = salarioRepository.findById(salarioDetalle.getSalario()).orElseThrow(()->new SalarioNotFoundException(salarioDetalle.getSalario()));
		
		SalarioDetalle salarioDetalleBBDD = new SalarioDetalle();
		
		salarioDetalleBBDD.setBase(salarioDetalle.getBase());
		salarioDetalleBBDD.setProrrata(salarioDetalle.getProrrata());
		salarioDetalleBBDD.setResidencia(salarioDetalle.getResidencia());
		salarioDetalleBBDD.setTotal(salarioDetalle.getTotal());
		salarioDetalleBBDD.setGrupo(salarioDetalle.getGrupo());
		salarioDetalleBBDD.setSalario(salario);
		
		salarioDetalleBBDD=salarioDetalleRepository.save(salarioDetalleBBDD);
		
		salario.getDetalles().add(salarioDetalleBBDD);
		
		salarioRepository.save(salario);
		
		return salarioDetalleBBDD;
	}
	
	

}
