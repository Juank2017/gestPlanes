package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@SQLDelete(sql = "UPDATE organismo SET deleted=true, deleted_at= NOW() WHERE id_organismo=?")
@EntityListeners(AuditingEntityListener.class)
public class Organismo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrganismo;
	
	private String organismo;
	
	private String nombreCortoOrganismo;
	
	
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="idPlan")
	private Plan idPlan;
	
	
	@JsonManagedReference
	@OneToMany(mappedBy = "entidad", cascade= CascadeType.ALL)
	private List<Contrato>contratos;
	
	

	@JsonIgnore
	@OneToMany(mappedBy = "organismo" , 
            cascade = CascadeType.ALL)
	private Set<OrganismoOcupacion> organismoOcupacion = new HashSet<OrganismoOcupacion>();
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;
	
}
