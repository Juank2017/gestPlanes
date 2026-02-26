package com.melilla.gestPlanes.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.TipoContingencia;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.ParteBajaRepository;
import com.melilla.gestPlanes.repository.ParteConfirmacionRepository;
import com.melilla.gestPlanes.repository.TipoContingeciaRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
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
	
	@Autowired
	private CiudadanoService ciudadanoService;
	
	

	@Override
	public List<ParteBaja> obtenerPartesBajaTrabajador(long idTrabajador) {
		
		return parteBajaRepository.findAllByCiudadanoIdCiudadano(idTrabajador) ;
	}
	
	@Override
	public List<Map<String, String>> obtenerPartesBajaTrabajadorMap(long idTrabajador) {
		
		
		List<ParteBaja> partes = parteBajaRepository.findAllByCiudadanoIdCiudadano(idTrabajador) ;
		
		List<Map<String, String>> salida = listaParteBajaToListaParteBajaPlana(partes);
		
		
		return salida;
	}

	private List<Map<String, String>> listaParteBajaToListaParteBajaPlana(List<ParteBaja> partes) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu", new Locale("es","ES"));
		
		List<Map<String,String>> salida = new ArrayList<Map<String,String>>();
		
		
		
		Iterator<ParteBaja> it = partes.iterator();
		
		while (it.hasNext()) {
			
			Map<String,String> partePlano = new HashMap<String,String>();
			
			ParteBaja parte = it.next(); 
			
			if(!parte.isDeleted()) {
				
				partePlano.put("idCiudadano", parte.getCiudadano().getIdCiudadano().toString());
				partePlano.put("dni", parte.getCiudadano().getDNI());
				partePlano.put("nombre", parte.getCiudadano().getNombre());
				partePlano.put("apellido1", parte.getCiudadano().getApellido1());
				partePlano.put("apellido2", parte.getCiudadano().getApellido2());
				
				partePlano.put("idParteBaja", parte.getIdParteBaja().toString());
				partePlano.put("fechaInicioBaja",(parte.getFechaInicioBaja() != null)?parte.getFechaInicioBaja().format(dtf):"");
				partePlano.put("fechaFinBaja",(parte.getFechaFinBaja()!= null)? parte.getFechaFinBaja().format(dtf):"");
				partePlano.put("contingencia", parte.getContingencia().getIdTipoContingencia()+" - "+ parte.getContingencia().getContingencia());
				
				if(!parte.getPartesConfirmacion().isEmpty()) {
					
					List<ParteConfirmacion> partesConfirmacion = parte.getPartesConfirmacion();
					
					for (int i = 0; i < partesConfirmacion.size(); i++) {
						
						ParteConfirmacion parteConfirmacion = partesConfirmacion.get(i);
						
						partePlano.put("conf"+(i+1),(parteConfirmacion.getFechaParteConfirmacion()!= null)? parteConfirmacion.getFechaParteConfirmacion().format(dtf):"");
						partePlano.put("idConf"+(i+1), parteConfirmacion.getIdParteConfirmacion().toString());
						
					}
					
					
				}
				salida.add(partePlano);
				
			}
			
			
		}
		return salida;
	}

	@Override
	public List<ParteBaja> obtenerPartesBajaTrabajadorPorDNI(String DNI) {
		
		return parteBajaRepository.findAllByCiudadanoDNI(DNI);
	}

	@Override
	public ParteBaja altaParteBaja(CrearParteBajaDTO parte) {

		ParteBaja nuevoParte = new ParteBaja();
		
		Ciudadano trabajador = ciudadanoRepository.findById(parte.getIdCiudadano()).orElseThrow(()-> new CiudadanoNotFoundException(parte.getIdCiudadano()));
		
		

		nuevoParte.setContingencia(tipoContingenciaRepository.findById(parte.getIdTipoContingencia()).orElseThrow(()-> new GenericNotFoundException()));
		
		nuevoParte.setFechaInicioBaja(parte.getFechaInicioBaja());
		
		nuevoParte.setCiudadano(trabajador);
		
		
		
		
		
		nuevoParte = parteBajaRepository.save(nuevoParte);
		
		trabajador.setBajaLaboral(ciudadanoService.estaDeBaja(trabajador.getIdCiudadano()));
		ciudadanoRepository.save(trabajador);
		
		return nuevoParte;
		
	}

	@Override
	public ParteBaja editaParteBaja(EditaParteBajaDTO parte) {
		
		ParteBaja parteAntiguo = parteBajaRepository.findById(parte.getIdParteBaja()).orElseThrow(()->new GenericNotFoundException());
		
		parteAntiguo.setContingencia(tipoContingenciaRepository.findById(parte.getIdTipoContingencia()).orElseThrow(()-> new GenericNotFoundException()));
		
		parteAntiguo.setFechaFinBaja(parte.getFechaFinBaja());
		
		parteAntiguo.setFechaInicioBaja(parte.getFechaInicioBaja());
		
		parteAntiguo =parteBajaRepository.save(parteAntiguo);
		
		parteAntiguo.getCiudadano().setBajaLaboral(ciudadanoService.estaDeBaja(parteAntiguo.getCiudadano().getIdCiudadano()));
		
		ciudadanoRepository.save(parteAntiguo.getCiudadano());
		
		return parteAntiguo;
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

	@Override
	public int numeroMaximoPartesConfirmacionTrabajador(long idTrabajador) {
		
		List<ParteBaja> partes = parteBajaRepository.findAllByCiudadanoIdCiudadano(idTrabajador);
		
		List<Integer> listaCantidadesPartesConfirmacion = new ArrayList<Integer>();
		
		if(!partes.isEmpty()) {
			
			for (ParteBaja parte : partes) {
				
				listaCantidadesPartesConfirmacion.add(parte.getPartesConfirmacion().size());
				
			}
			
		}
		
		Collections.sort(listaCantidadesPartesConfirmacion,Collections.reverseOrder());
		
		return (listaCantidadesPartesConfirmacion.isEmpty())?0: listaCantidadesPartesConfirmacion.get(0);
	}

	@Override
	public List<Map<String,String>> obtenerPartesBajaPlan(Plan WorkingPlan) {
		
		List<ParteBaja> partesBajaPlanActivo = parteBajaRepository.findAllByCiudadanoIdPlan(WorkingPlan);
		
		
	List<Map<String, String>> salida = listaParteBajaToListaParteBajaPlana(partesBajaPlanActivo);
		
		
		return salida;
	}

	@Override
	public void borrarParteBaja(long idParteBaja) {

		ParteBaja parteBaja = parteBajaRepository.findById(idParteBaja).orElseThrow(()-> new GenericNotFoundException());
		
		long idCiudadano = parteBaja.getCiudadano().getIdCiudadano();
		
		parteBajaRepository.delete(parteBaja);
		
		Ciudadano trabajador = ciudadanoRepository.findById(idCiudadano).orElseThrow(()-> new CiudadanoNotFoundException(idCiudadano));
		
		trabajador.setBajaLaboral(ciudadanoService.estaDeBaja(trabajador.getIdCiudadano()));
		
		ciudadanoRepository.save(trabajador);
		
		
	}

	@Override
	public void borraParteConfirmacion(long idParteConfirmacion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ParteConfirmacion> obtenerPartesConfirmacion(long idParteBaja) {
		
		return parteConfirmacionRepository.findAllByParteBajaIdParteBajaAndDeletedFalse(idParteBaja);
	}

	
	
	
	
	

}
