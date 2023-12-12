package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE vacaciones SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class Vacaciones {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idVacaciones;
	
	private Date FechaInicio;
	
	private Date FechaFin;
	
	private int dias;
	
	@ManyToOne
	@JoinColumn(name="idContrato")
	private Contrato contrato;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	
}
