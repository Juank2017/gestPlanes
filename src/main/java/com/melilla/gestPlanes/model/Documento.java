package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.TimeZone;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
//@Audited
@SQLDelete(sql = "UPDATE documento SET deleted=true, deleted_at= NOW() WHERE id_documento=?")
@EntityListeners(AuditingEntityListener.class)
public class Documento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDocumento;
	
	private String ruta;
	
	private String nombre;
	
	private String tipo;
	
	private String observaciones;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	@NotAudited
	private Plan idPlan;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
