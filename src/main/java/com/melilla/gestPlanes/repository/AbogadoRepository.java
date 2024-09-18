package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Abogado;

public interface AbogadoRepository extends JpaRepository<Abogado, Long>, RevisionRepository<Abogado,Long,Long>{

}
