package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@SQLDelete(sql = "UPDATE contrato SET deleted=true, deleted_at= NOW() WHERE id=?")
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContrato;
	
	
	private LocalDate fechaInicio;
	
	
	private LocalDate fechaFinal;
	
	
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
	
	@NotAudited
	@OneToMany(mappedBy = "contrato" , cascade= CascadeType.ALL)
	private List<Vacaciones> periodosVacaciones;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	

}
