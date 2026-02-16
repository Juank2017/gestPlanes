package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearParteBajaDTO;
import com.melilla.gestPlanes.DTO.CrearParteConfirmacionDTO;
import com.melilla.gestPlanes.DTO.EditaContingenciaDTO;
import com.melilla.gestPlanes.DTO.EditaParteBajaDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.GenericNotFoundException;
import com.melilla.gestPlanes.model.ParteBaja;
import com.melilla.gestPlanes.model.ParteConfirmacion;
import com.melilla.gestPlanes.model.TipoContingencia;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.ParteBajaRepository;
import com.melilla.gestPlanes.repository.ParteConfirmacionRepository;
import com.melilla.gestPlanes.repository.TipoContingeciaRepository;
import com.melilla.gestPlanes.service.ParteBajaService;

@Service
public class ParteBajaServiceIMPL implements ParteBajaService {
	
	@Autowired
	private CiudadanoRepository ciudadanoRepository;
	
	@Autowired
	private ParteBajaRepository parteBajaRepository;
	
	@Autowired
	private TipoContingeciaRepository tipoContingenciaRepository;
	
	@Autowired
	private ParteConfirmacionRepository parteConfirmacionRepository;
	
	

	@Override
	public List<ParteBaja> obtenerPartesBajaTrabajador(long idTrabajador) {
		
		return parteBajaRepository.findAllByCiudadanoIdCiudadano(idTrabajador) ;
	}

	@Override
	public List<ParteBaja> obtenerPartesBajaTrabajadorPorDNI(String DNI) {
		
		return parteBajaRepository.findAllByCiudadanoDNI(DNI);
	}

	@Override
	public ParteBaja altaParteBaja(CrearParteBajaDTO parte) {

		ParteBaja nuevoParte = new ParteBaja();
		
		nuevoParte.setCiudadano(ciudadanoRepository.findById(parte.getIdCiudadano()).orElseThrow(()-> new CiudadanoNotFoundException(parte.getIdCiudadano())));

		nuevoParte.setContingencia(tipoContingenciaRepository.findById(parte.getIdTipoContingencia()).orElseThrow(()-> new GenericNotFoundException()));
		
		nuevoParte.setFechaInicioBaja(parte.getFechaInicioBaja());
		
		//nuevoParte.setFechaFinBaja(parte.getFechaFinBaja());
		
		return parteBajaRepository.save(nuevoParte);
		
	}

	@Override
	public ParteBaja editaParteBaja(EditaParteBajaDTO parte) {
		
		ParteBaja parteAntiguo = parteBajaRepository.findById(parte.getIdParteBaja()).orElseThrow(()->new GenericNotFoundException());
		
		parteAntiguo.setContingencia(tipoContingenciaRepository.findById(parte.getIdTipoContingencia()).orElseThrow(()-> new GenericNotFoundException()));
		
		parteAntiguo.setFechaFinBaja(parte.getFechaFinBaja());
		
		parteAntiguo.setFechaInicioBaja(parte.getFechaInicioBaja());
		
		return parteBajaRepository.save(parteAntiguo);
	}

	@Override
	public ParteBaja insertaParteConfirmacion(CrearParteConfirmacionDTO parte) {
		
		ParteBaja parteBaja = parteBajaRepository.findById(parte.getIdParteBaja()).orElseThrow(()-> new GenericNotFoundException());
		
		ParteConfirmacion parteConfirmacion = new ParteConfirmacion();
		
		parteConfirmacion.setParteBaja(parteBaja);
		parteConfirmacion.setFechaParteConfirmacion(parte.getFechaParteConfirmacion());
		
		parteBaja.getPartesConfirmacion().add(parteConfirmacionRepository.save(parteConfirmacion));
		
		
		return parteBajaRepository.save(parteBaja);
	}

	@Override
	public List<TipoContingencia> listaContingencias() {
		
		return tipoContingenciaRepository.findAll();
	}

	@Override
	public TipoContingencia crearContingencia(String contingencia) {
		
		TipoContingencia nuevoTipo = new TipoContingencia();
		
		nuevoTipo.setContingencia(contingencia);
		
		
		return tipoContingenciaRepository.save(nuevoTipo);
	}

	@Override
	public TipoContingencia editarContingencia(EditaContingenciaDTO contingencia) {
		
		TipoContingencia tipo = tipoContingenciaRepository.findById(contingencia.getIdContingencia()).orElseThrow(()-> new GenericNotFoundException());
		
		tipo.setContingencia(contingencia.getContingencia());
		
		
		
		return tipoContingenciaRepository.save(tipo);
	}

	@Override
	public void borraContingencia(long idTipoContingencia) {
		
		TipoContingencia tipo = tipoContingenciaRepository.findById(idTipoContingencia).orElseThrow(()-> new GenericNotFoundException());
		
		
		tipoContingenciaRepository.delete(tipo);
	}
	
	
	
	

}
