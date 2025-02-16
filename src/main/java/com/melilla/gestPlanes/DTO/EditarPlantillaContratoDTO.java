package com.melilla.gestPlanes.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EditarPlantillaContratoDTO {


	private Long idContratoConfig;
	
	private String nombre;
	
	private String cif;
	//nombre y apellidos del representante
	private String nombreRepresentante;
	
	private String dniRepresentante;
	
	private String cargoRepresentante;
	
	private String razonSocial;
	
	private String domicilioSocial;
	
	private String paisEmpresa;
	
	private String codigoPaisEmpresa;
	
	private String municipioEmpresa;
	
	private String codigoMunicipioEmpresa;
	
	private String codigoPostalEmpresa;
	
	private String regimen;
	
	private String codigoCuentaCotizacion;
	
	private String actividadEconomica;
	
	private String horas;
	
	private String sepe;
	
	private String clausulaEspecifica;
	
	private String clausulaAdicional;
	
	
}
