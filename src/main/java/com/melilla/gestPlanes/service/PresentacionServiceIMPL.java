package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.PresentacionNotFoundException;
import com.melilla.gestPlanes.model.Presentacion;
import com.melilla.gestPlanes.repository.PresentacionRepository;

@Service
public class PresentacionServiceIMPL implements PresentacionService {

	@Autowired
	private PresentacionRepository presentacionRepository;

	@Override
	public Presentacion crearPresentacion(Presentacion presentacion) {

		return presentacionRepository.save(presentacion);
	}

	@Override
	public List<Presentacion> presentaciones() {

		return presentacionRepository.findAll(Sort.by("presentacion").ascending());
	}

	@Override
	public Presentacion actualizarPresentacion(Presentacion presentacion) {

		Presentacion presentacionBBDD = presentacionRepository.findById(presentacion.getIdPresentacion())
				.orElseThrow(() -> new PresentacionNotFoundException());

		presentacionBBDD.setPresentacion(presentacion.getPresentacion());
		presentacionBBDD.setResponsable(presentacion.getResponsable());
		presentacionBBDD.setVacaciones(presentacion.getVacaciones());
		presentacionBBDD.setObservaciones(presentacion.getObservaciones());

		return presentacionRepository.save(presentacionBBDD);
	}

	@Override
	public void borrarPresentacion(Long idPresentacion) {
		presentacionRepository.deleteById(idPresentacion);

	}

	@Override
	public Presentacion buscarPresentacion(Long idPresentacion) {

		return presentacionRepository.findById(idPresentacion).orElseThrow(() -> new PresentacionNotFoundException());
	}

}
