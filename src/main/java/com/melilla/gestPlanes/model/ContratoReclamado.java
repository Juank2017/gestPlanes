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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idContratoReclamado")
@SQLDelete(sql = "UPDATE contrato_reclamado SET deleted=true, deleted_at= NOW() WHERE id_contrato_reclamado=?")
@EntityListeners(AuditingEntityListener.class)
public class ContratoReclamado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContratoReclamado;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaInicio;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaFinal;
	

	
	private String gc;


	@JsonManagedReference
	@OneToMany(mappedBy = "contrato", cascade=CascadeType.ALL)
	private List<NominasReclamadas> nominasReclamadas = new ArrayList<>();
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idProcedimiento")
	private Procedimiento procedimiento;


	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	



	
	
}
