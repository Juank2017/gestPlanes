package com.melilla.gestPlanes.model.config;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.melilla.gestPlanes.model.Plan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PlantillaContratoConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContratoConfig;
	
	private String nombre;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idPlan")
	private Plan plan;
	
	boolean activa;
	
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
	
	private String textoConceptosSalariales;
	@Column(columnDefinition="TEXT")
	private String clausulaEspecifica;
	@Column(columnDefinition="TEXT")
	private String clausulaAdicional;


}
