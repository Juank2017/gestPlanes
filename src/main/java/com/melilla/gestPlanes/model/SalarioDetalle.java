package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Table(name="salario_detalle")
@SQLDelete(sql = "UPDATE salario_detalle SET deleted=true, deleted_at= NOW() WHERE id_salario_detalle=?")
@EntityListeners(AuditingEntityListener.class)
public class SalarioDetalle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idSalarioDetalle;
	
	private int grupo;
	
	private String base;
	
	private String prorrata;
	
	private String residencia;
	
	private String total;
	
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name="idSalario")
	private Salario salario;
	
	
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
