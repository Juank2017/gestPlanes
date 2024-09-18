package com.melilla.gestPlanes.model;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idNomina")
@SQLDelete(sql = "UPDATE nomina_reclamada SET deleted=true, deleted_at= NOW() WHERE id_Nomina=?")
@EntityListeners(AuditingEntityListener.class)
public class NominasReclamadas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNomina;
	
	private String mes;
	
	private String year;
	
	//Importes que ha percibido
	private BigDecimal basePercibida;
	
	private BigDecimal prorrataPercibida;
	
	private BigDecimal residenciaPercibida;
	
	//Importes que reclama el demandante
	private BigDecimal baseDevengada;
	
	private BigDecimal prorrataDevengada;
	
	private BigDecimal residenciaDevengada;
	 
	//Importes que calcula personal
	private BigDecimal baseCalculada;
	
	private BigDecimal prorrataCalculada;
	
	private BigDecimal residenciaCalculada;
	
	//cantidad que reclama
	
	private BigDecimal diferenciaReclamada;
	
	//cantidad que se le reconoce
	
	private BigDecimal diferenciaCalculada;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="idContratoReclamado")
	private ContratoReclamado contrato;

}
