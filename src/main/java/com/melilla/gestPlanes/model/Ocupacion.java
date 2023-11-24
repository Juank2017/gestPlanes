package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE ocupacion SET deleted=true, deleted_at= NOW() WHERE id=?")
public class Ocupacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOcupacion;
	
	private String ocupacion;
	
	private String ocupacionSEPE;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="idCategoria")
	private Categoria categoria;
	

	
	@JsonIgnore
	@OneToMany(mappedBy = "ocupacion", cascade= CascadeType.ALL)
	private List<Contrato>contratos;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@OneToMany(mappedBy = "ocupacion",
            cascade = CascadeType.ALL)
	private Set<OrganismoOcupacion> organismoOcupacion = new HashSet<OrganismoOcupacion>();
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
