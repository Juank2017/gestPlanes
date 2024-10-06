package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import jakarta.persistence.OneToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idAbogado")
@SQLDelete(sql = "UPDATE abogado SET deleted=true, deleted_at= NOW() WHERE id_abogado=?")
@EntityListeners(AuditingEntityListener.class)
public class Abogado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAbogado;
	
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;

	private String numeroColegiado;
	
	private String telefono;
	
	private String email;
	
	
	@JsonBackReference
	@OneToMany(mappedBy = "abogado", cascade = CascadeType.ALL)
	private List<Procedimiento> procedimiento;
	
	
	
}
