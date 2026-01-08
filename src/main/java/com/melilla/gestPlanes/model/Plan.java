package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;

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
@Audited
@SQLDelete(sql = "UPDATE plan SET deleted=true, deleted_at= NOW() WHERE id_Plan=?")
@EntityListeners(AuditingEntityListener.class)
public class Plan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlan;
	
	private String denominacion;
	
	private boolean activo;
	
	@OneToMany(mappedBy = "plan")
	private List<Salario> salario;
	
	@OneToMany(mappedBy="plan")
	private List<PlantillaContratoConfig>plantillas;
	
	//@OneToOne(cascade = CascadeType.ALL, mappedBy = "plan" )
    //@JoinColumn(name = "config_id", referencedColumnName = "idConfig")
	//@OneToOne(cascade = CascadeType.ALL )
   // @JoinColumn(name = "config_id", referencedColumnName = "plan")
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "config_id", referencedColumnName = "idConfig")
	private PlanConfig config;
	

	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
