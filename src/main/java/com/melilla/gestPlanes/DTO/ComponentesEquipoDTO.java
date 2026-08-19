package com.melilla.gestPlanes.DTO;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComponentesEquipoDTO {

	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String telefono;
	private String turno;
	private String ocupacion;
	private String organismo;
	private String destino;
	private long idCiudadano;
	private boolean jefe;
	
	
	public static ComponentesEquipoDTO ciudadanoToComponentesEquipoDTO (Ciudadano ciudadano) {
		
		ComponentesEquipoDTO componente = new ComponentesEquipoDTO();
		
		String turno ="";
		String ocupacion = "";
		String organismo = "";
		String destino = "";
		
		componente.setNombre(ciudadano.getNombre());
		componente.setApellido1(ciudadano.getApellido1());
		componente.setApellido2(ciudadano.getApellido2());
		componente.setDni(ciudadano.getDNI());
		componente.setTelefono(ciudadano.getTelefono());
		if (ciudadano.getContrato() != null) {
			Contrato contrato = ciudadano.getContrato();
			
			turno = contrato.getTurno() != null?contrato.getTurno():"";
			ocupacion = contrato.getOcupacion() != null?contrato.getOcupacion().getOcupacion():"";
			organismo = contrato.getEntidad() != null?contrato.getEntidad().getNombreCortoOrganismo():"";
			destino = contrato.getDestino() != null ? contrato.getDestino().getDestino():"";
		}
		componente.setTurno( turno);
		componente.setOcupacion(ocupacion);
		componente.setOrganismo(organismo);
		componente.setDestino(destino);
		componente.setIdCiudadano(ciudadano.getIdCiudadano());
		componente.setJefe(ciudadano.isEsJefeEquipo());
		
		return componente;
		
	}
}
