package com.melilla.gestPlanes.model;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProcedimiento")
@SQLDelete(sql = "UPDATE procedimiento SET deleted=true, deleted_at= NOW() WHERE id_procedmiento=?")
@EntityListeners(AuditingEntityListener.class)
public class Procedimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProcedimiento;
	
	private String numeroProcedimiento;
	
	@ManyToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	@ManyToOne
	@JoinColumn(name="idAbogado")
	private Abogado abogado;
	
	

}
