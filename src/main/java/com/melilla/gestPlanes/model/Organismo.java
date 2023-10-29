package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE organismo SET deleted=true, deleted_at= NOW() WHERE id=?")
public class Organismo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrganismo;
	
	private String organismo;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	private Plan idPlan;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
}
