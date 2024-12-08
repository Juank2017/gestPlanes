package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.ResponseSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.UpdateDetalleSalarioDTO;
import com.melilla.gestPlanes.exceptions.exceptions.SalarioDetalleNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.SalarioDetalle;
import com.melilla.gestPlanes.repository.SalarioDetalleRepository;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioDetalleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarioDetalleServiceImpl implements SalarioDetalleService {

	@Autowired
	private SalarioDetalleRepository salarioDetalleRepository;

	@Autowired
	private SalarioRepository salarioRepository;

	@Override
	public List<ResponseSalarioDetalleDTO> obtenerDetalleSalario(long idSalario) {

		List<SalarioDetalle> listado = salarioDetalleRepository.findAllBySalarioIdSalario(idSalario);

		List<ResponseSalarioDetalleDTO> salida = new ArrayList<ResponseSalarioDetalleDTO>();

		for (SalarioDetalle salarioDetalle : listado) {

			ResponseSalarioDetalleDTO response = new ResponseSalarioDetalleDTO();
			if(!salarioDetalle.isDeleted()) {
				response.setIdSalarioDetalle(salarioDetalle.getIdSalarioDetalle() + "");
				response.setGrupo(salarioDetalle.getGrupo() + "");
				response.setBase(salarioDetalle.getBase());
				response.setProrrata(salarioDetalle.getProrrata());
				response.setResidencia(salarioDetalle.getResidencia());
				response.setTotal(salarioDetalle.getTotal());
				salida.add(response);
			}


		}

		return salida;
	}

	@Override
	public SalarioDetalle actualizarDetalleSalario(UpdateDetalleSalarioDTO salarioDetalle) {
		
		SalarioDetalle detalleSalario = salarioDetalleRepository.findById(salarioDetalle.getIdSalarioDetalle()).orElseThrow(()->new SalarioDetalleNotFoundException(salarioDetalle.getIdSalarioDetalle()));
		
		detalleSalario.setGrupo(salarioDetalle.getGrupo());
		detalleSalario.setBase(salarioDetalle.getBase());
		detalleSalario.setProrrata(salarioDetalle.getProrrata());
		detalleSalario.setResidencia(salarioDetalle.getResidencia());
		detalleSalario.setTotal(salarioDetalle.getTotal());
		
		
		return salarioDetalleRepository.save(detalleSalario);
	}

	@Override
	public SalarioDetalle crearSalarioDetalle(CrearSalarioDetalleDTO salarioDetalle) {

		Salario salario = salarioRepository.findById(salarioDetalle.getIdSalario())
				.orElseThrow(() -> new SalarioNotFoundException(salarioDetalle.getIdSalario()));

		SalarioDetalle salarioDetalleBBDD = new SalarioDetalle();

		salarioDetalleBBDD.setBase(salarioDetalle.getBase());
		salarioDetalleBBDD.setProrrata(salarioDetalle.getProrrata());
		salarioDetalleBBDD.setResidencia(salarioDetalle.getResidencia());
		salarioDetalleBBDD.setTotal(salarioDetalle.getTotal());
		salarioDetalleBBDD.setGrupo(salarioDetalle.getGrupo());
		salarioDetalleBBDD.setSalario(salario);

		salarioDetalleBBDD = salarioDetalleRepository.save(salarioDetalleBBDD);

		salario.getDetalles().add(salarioDetalleBBDD);

		salarioRepository.save(salario);

		return salarioDetalleBBDD;
	}

	@Override
	public List<ResponseSalarioDetalleDTO> obtenerDetalleSalarioActivo(long idPlan) {

		Salario salario = salarioRepository.findByPlanIdPlanAndActivo(idPlan, true);
		
		List<ResponseSalarioDetalleDTO> salida = listarDetalleSalario(salario);

		return salida;
	}

	private List<ResponseSalarioDetalleDTO> listarDetalleSalario(Salario salario) {
		List<SalarioDetalle> listado = salario.getDetalles(); 

		List<ResponseSalarioDetalleDTO> salida = listarDetalleSalario(salario);
		new ArrayList<ResponseSalarioDetalleDTO>();

		for (SalarioDetalle salarioDetalle : listado) {

			ResponseSalarioDetalleDTO response = new ResponseSalarioDetalleDTO();

			if(!salarioDetalle.isDeleted()) {
			
				response.setIdSalarioDetalle(salarioDetalle.getIdSalarioDetalle() + "");
				response.setGrupo(salarioDetalle.getGrupo() + "");
				response.setBase(salarioDetalle.getBase());
				response.setProrrata(salarioDetalle.getProrrata());
				response.setResidencia(salarioDetalle.getResidencia());
				response.setTotal(salarioDetalle.getTotal());
				salida.add(response);

			}
			
		}
		return salida;
	}

	@Override
	public List<ResponseSalarioDetalleDTO> borraDetalleSalario(long idSalarioDetalle) {
		
		SalarioDetalle detalle = salarioDetalleRepository.findById(idSalarioDetalle).orElseThrow(()->new SalarioDetalleNotFoundException(idSalarioDetalle));
		
		
		
		
		

		salarioDetalleRepository.deleteById(idSalarioDetalle);
		
		List<SalarioDetalle> listado = detalle.getSalario().getDetalles(); 
		
		List<ResponseSalarioDetalleDTO> salida = new ArrayList<ResponseSalarioDetalleDTO>();

		for (SalarioDetalle salarioDetalle : listado) {
			ResponseSalarioDetalleDTO response = new ResponseSalarioDetalleDTO();
			if(!salarioDetalle.isDeleted()) {


				response.setIdSalarioDetalle(salarioDetalle.getIdSalarioDetalle() + "");
				response.setGrupo(salarioDetalle.getGrupo() + "");
				response.setBase(salarioDetalle.getBase());
				response.setProrrata(salarioDetalle.getProrrata());
				response.setResidencia(salarioDetalle.getResidencia());
				response.setTotal(salarioDetalle.getTotal());
				salida.add(response);
	
			}
			
		}

		return salida;
	}

}
