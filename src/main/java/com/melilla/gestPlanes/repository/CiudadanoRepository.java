package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Ciudadano;


public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long> ,RevisionRepository<Ciudadano, Long, Long> {

}
