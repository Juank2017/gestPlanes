package com.melilla.gestPlanes.model;



import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@SQLDelete(sql = "UPDATE embargo_ciudadano SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class EmbargoCiudadano {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long IdEmbargo;
	
	private Long cantidad;
	
	private String organismo;
	
	
	@ManyToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	private String tipo;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	
}
