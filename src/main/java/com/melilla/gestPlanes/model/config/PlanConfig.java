package com.melilla.gestPlanes.model.config;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.melilla.gestPlanes.model.Plan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PlanConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idConfig;
	
	private String uploadDir;
	
	private String tempDir;
	
	private String trashcanDir;
	
	private String templateDir;

	
	@JsonIgnore
	@OneToOne(mappedBy = "config")
	private Plan plan;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
	
}
