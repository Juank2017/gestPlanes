package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Audited
@SQLDelete(sql = "UPDATE documento SET deleted=true, deleted_at= NOW() WHERE id=?")
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
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
