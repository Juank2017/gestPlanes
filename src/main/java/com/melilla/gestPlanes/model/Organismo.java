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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
	
	@JsonIgnore
	@OneToMany(mappedBy = "entidad", cascade= CascadeType.ALL)
	private List<Contrato>contratos;
	
	

	
	@OneToMany(mappedBy = "organismo",
            cascade = CascadeType.ALL)
	private Set<OrganismoOcupacion> organismoOcupacion = new HashSet<OrganismoOcupacion>();
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
}
