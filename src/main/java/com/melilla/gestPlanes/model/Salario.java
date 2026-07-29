package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;

@Entity
@Audited
@Data
@SQLDelete(sql = "UPDATE salario SET deleted=true, deleted_at= NOW() WHERE id_salario=?")
@EntityListeners(AuditingEntityListener.class)
public class Salario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSalario;
	
	private String descripcion;
	
	@JsonBackReference
	@OneToMany(mappedBy = "salario", cascade = CascadeType.ALL)
	@NotAudited
	private List<SalarioDetalle> detalles;
	
	
	private boolean activo;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idPlan")
	private Plan plan;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	
}
