package com.melilla.gestPlanes.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Equipo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEquipo;
	
	@OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
	private List<Ciudadano> componentes;
	
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano jefeEquipo;
}
