package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE salario SET deleted=true, deleted_at= NOW() WHERE id=?")
public class Salario {

	@Id
	private int grupo;
	
	private String base;
	
	private String prorrata;
	
	private String residencia;
	
	private String total;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	private Plan plan;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
}
