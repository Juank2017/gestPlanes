//package com.melilla.gestPlanes.service.impl;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import com.melilla.gestPlanes.model.Expediente;
//import com.melilla.gestPlanes.repository.ExpedienteRepository;
//import com.melilla.gestPlanes.service.ExpedienteService;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ExpedienteServiceImpl implements ExpedienteService {
//
//	@Autowired
//	private ExpedienteRepository expedienteRepository;
//	
//	@Override
//	public List<Expediente> getExpedientes() {
//		
//		return expedienteRepository.findAll();
//	}
//
//	@Override
//	public Optional<Expediente> getExpedientaByCiudadanoDNI(String DNI) {
//		
//		return Optional.ofNullable(expedienteRepository.findByInteresadosDNI(DNI));
//	}
//
//	@Override
//	public List<Expediente> getExpedienteByOcupacion(String ocupacion) {
//		
//		return  expedienteRepository.findAllByInteresadosContratoOcupacion(ocupacion);
//	}
//
//}
