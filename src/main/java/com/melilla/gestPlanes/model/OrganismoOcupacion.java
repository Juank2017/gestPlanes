package com.melilla.gestPlanes.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Organismos_ocupaciones")
@Data
public class OrganismoOcupacion {
	@Id
	private Long id;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idOrganismo") 
	private Organismo organismo;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idOcupacion") 
	private Ocupacion ocupacion;

	private int nTrabajadores;

	

}
