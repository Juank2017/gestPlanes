package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE plan SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class Plan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlan;
	
	private String denominacion;
	
	private boolean activo;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
