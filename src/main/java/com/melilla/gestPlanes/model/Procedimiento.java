package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProcedimiento")
@SQLDelete(sql = "UPDATE procedimiento SET deleted=true, deleted_at= NOW() WHERE id_procedmiento=?")
@EntityListeners(AuditingEntityListener.class)
public class Procedimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProcedimiento;
	
	private String numeroProcedimiento;
	
	private String sentencia;
	
	private LocalDate fechaSentencia;
	
	@JsonManagedReference
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@ManyToOne
	@JsonManagedReference
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	@JoinColumn(name="idAbogado")
	private Abogado abogado;
	
	
	@OneToMany(mappedBy = "procedimiento",  cascade=CascadeType.ALL)
	private List<ContratoReclamado>periodos = new ArrayList<>();
	

	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
