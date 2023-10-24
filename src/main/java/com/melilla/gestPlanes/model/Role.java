package com.melilla.gestPlanes.model;

import java.util.Collection;


import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String roleName;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Collection<User> users;


    
}
