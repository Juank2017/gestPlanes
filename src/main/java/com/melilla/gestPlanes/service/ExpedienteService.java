package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import com.melilla.gestPlanes.model.Expediente;

public interface ExpedienteService {

	List<Expediente> getExpedientes();
	Optional<Expediente>getExpedientaByCiudadanoDNI(String DNI);
	List<Expediente>getExpedienteByOcupacion(String ocupacion);
}
