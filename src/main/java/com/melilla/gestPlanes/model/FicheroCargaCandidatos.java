package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE fichero_carga_candidatos SET deleted=true, deleted_at= NOW() WHERE id_fichero_carga_candidatos=?")
@EntityListeners(AuditingEntityListener.class)
public class FicheroCargaCandidatos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idFicheroCargaCandidatos;
	
	private String fileName;
	
	private boolean procesado;
	
	private String URL;
	
	@OneToOne
	@JoinColumn(name="id_fichero_resultado", referencedColumnName = "idFicheroCargaCandidatos")
	private FicheroCargaCandidatos resultadoCarga;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="idPlan")
	@NotAudited
	private Plan idPlan;
	
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	
}
