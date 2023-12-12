package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE nota_ciudadano SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class NotaCiudadano {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNota;
	
	@Temporal(TemporalType.DATE)
	private Date fechaNota;
	
	private String nota;
	
	private boolean pinned;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
