package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Audited
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE contrato SET deleted=true, deleted_at= NOW() WHERE id_contrato=?")
@EntityListeners(AuditingEntityListener.class)
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContrato;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaInicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaFinal;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaExtincion;
	
	
	@NotAudited
	@ManyToOne
	@JoinColumn(name="idOcupacion")
	private Ocupacion ocupacion;
	
	
	@NotAudited
	@ManyToOne
	@JoinColumn(name="idCategoria")
	private Categoria categoria;
	

	private String gc;
	
	@NotAudited
	@ManyToOne
	@JoinColumn(name="idOrganismo")
	private Organismo entidad;
	
	@NotAudited
	@ManyToOne
	@JoinColumn(name="idDestino")
	private Destino destino;
	
	private int duracion;
	
	private String porcentajeHoras;
	
	private String base;
	
	private String prorratas;
	
	private String residencia;
	
	private String total;
	
	private String turno;
	
	private int diasVacaciones;
	
	
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	

}
