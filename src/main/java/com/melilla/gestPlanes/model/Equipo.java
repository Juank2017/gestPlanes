package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE equipo SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class Equipo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEquipo;
	
	@OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
	private List<Ciudadano> componentes;
	
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano jefeEquipo;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
