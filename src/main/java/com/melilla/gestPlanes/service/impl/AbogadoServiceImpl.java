package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearAbogadoDto;
import com.melilla.gestPlanes.model.Abogado;
import com.melilla.gestPlanes.repository.AbogadoRepository;
import com.melilla.gestPlanes.service.AbogadoService;

@Service
public class AbogadoServiceImpl implements AbogadoService {

	@Autowired
	private AbogadoRepository abogadoRepository;
	
	@Override
	public List<Abogado> abogados() {
		
		return abogadoRepository.findAll();
	}

	@Override
	public Abogado crearAbogado(CrearAbogadoDto abogado) {
		
		
		Abogado abogadoParaGuardar = new Abogado();
		
		abogadoParaGuardar.setNumeroColegiado(abogado.getNumeroColegiado());
		abogadoParaGuardar.setNombre(abogado.getNombre());
		abogadoParaGuardar.setApellido1(abogado.getApellido1());
		abogadoParaGuardar.setApellido2(abogado.getApellido2());
		abogadoParaGuardar.setTelefono(abogado.getTelefono());
		abogadoParaGuardar.setEmail(abogado.getEmail());
		
		return abogadoRepository.save(abogadoParaGuardar);
		
	}

	@Override
	public void borrarAbogado(Long idAbogado) {

		abogadoRepository.deleteById(idAbogado);

	}

	@Override
	public Abogado getAbogado(Long idAbogado) {
		
		return abogadoRepository.getById(idAbogado);
	}

}
