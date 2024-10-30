package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
@Audited
@SQLDelete(sql = "UPDATE documento_procedimiento_reclamacion SET deleted=true, deleted_at= NOW() WHERE id_documento_procedimiento=?")
@EntityListeners(AuditingEntityListener.class)
public class DocumentoProcedimientoReclamacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDocumentoProcedimiento;
	
	private String ruta;
	
	private String nombre;
	
	private String tipo;
	
	private String observaciones;
	

	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idProcedimiento")
	private Procedimiento procedimiento;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
