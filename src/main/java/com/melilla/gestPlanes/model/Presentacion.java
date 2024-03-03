package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE presentacion SET deleted=true, deleted_at= NOW() WHERE idPresentacion=?")
@EntityListeners(AuditingEntityListener.class)
public class Presentacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPresentacion;

	private String presentacion;
	
	private String responsable;
	private String vacaciones;
	private String observaciones;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
