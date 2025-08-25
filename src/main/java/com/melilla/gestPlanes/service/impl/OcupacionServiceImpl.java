package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditarOcupacionDTO;
import com.melilla.gestPlanes.DTO.OcupacionResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionConContratosException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.service.OcupacionService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OcupacionServiceImpl implements OcupacionService {
	
	@Autowired
	private OcupacionRepository ocupacionRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	PlanService planService;
	
	
	@Override
	public List<Ocupacion> obtenerOcupacionesPorCategoria(Long idCategoria) {
		
		return ocupacionRepository.findAllByCategoriaIdCategoriaOrderByOcupacionAsc(idCategoria);
	}


	@Override
	public List<OcupacionResponseDTO> obtenerOcupacionesPlan(Plan idPlan) {
		
		List<OcupacionResponseDTO> result = new ArrayList<OcupacionResponseDTO>();
		
		List<Ocupacion> ocupaciones = ocupacionRepository.findAllByIdPlan(idPlan);
		
		Iterator<Ocupacion> it = ocupaciones.iterator();
		
		while (it.hasNext()) {
			Ocupacion ocupacion = it.next();
			
			OcupacionResponseDTO dto = new OcupacionResponseDTO();
			
			dto.setCategoria(ocupacion.getCategoria().getCategoria());
			dto.setCatLab(ocupacion.getCategoria().getGrupoProfesionalPersonalLaboral());
			dto.setIdOcupacion(ocupacion.getIdOcupacion());
			dto.setOcupacion(ocupacion.getOcupacion());
			dto.setCno(ocupacion.getOcupacionSEPE());
			dto.setIdCategoria(ocupacion.getCategoria().getIdCategoria());
			result.add(dto);
		}
		
		return result;
	}


	@Override
	public void borrarOcupacion(Long idOcupacion) {

		Ocupacion ocupacion = ocupacionRepository.findById(idOcupacion).orElseThrow(()-> new OcupacionNotFoundException(idOcupacion));
		
		if (!ocupacion.getContratos().isEmpty()) {
			throw new OcupacionConContratosException(idOcupacion);
		}else
		{
			ocupacionRepository.delete(ocupacion);
		}
		
	}


	@Override
	public Ocupacion crearOcupacion(CrearOcupacionDTO ocupacion) {
		
		Categoria cat = categoriaRepository.findById(ocupacion.getIdCategoria()).orElseThrow(()-> new CategoriaNotFoundException(ocupacion.getIdCategoria()));
		
		Ocupacion nuevaOcupacion = new Ocupacion();
		
		nuevaOcupacion.setCategoria(cat);
		
		nuevaOcupacion.setOcupacion(ocupacion.getOcupacion());
		
		nuevaOcupacion.setOcupacionSEPE(ocupacion.getCno());
		
		nuevaOcupacion.setIdPlan(planService.getPlan(ocupacion.getIdPlan()).get());
		
		
		return ocupacionRepository.save(nuevaOcupacion);
	}


	@Override
	public Ocupacion editarOcupacion(EditarOcupacionDTO ocupacion) {
		
		Ocupacion ocup = ocupacionRepository.findById(ocupacion.getIdOcupacion()).orElseThrow(()-> new OcupacionNotFoundException(ocupacion.getIdOcupacion()));
		
		ocup.setOcupacion(ocupacion.getOcupacion());
		ocup.setOcupacionSEPE(ocupacion.getCno());
		
		
		
		return ocupacionRepository.save(ocup);
	}

}
