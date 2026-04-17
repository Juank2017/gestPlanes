package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@SQLDelete(sql = "UPDATE errores_carga_fichero_candidatos SET deleted=true, deleted_at= NOW() WHERE id_error=?")
@EntityListeners(AuditingEntityListener.class)
public class ErroresCargaFicheroCandidatos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idError;
	
	private String error;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idFicheroCargaCandidatos")
	private FicheroCargaCandidatos fichero;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	public ErroresCargaFicheroCandidatos(String error, FicheroCargaCandidatos fichero) {
		
		this.error = error;
		this.fichero= fichero;
		this.deleted=false;
		
		
	}

}
