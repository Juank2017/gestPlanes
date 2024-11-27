package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.PagoReclamacion;

public interface PagoReclamacionRepository extends JpaRepository<PagoReclamacion, Long>, RevisionRepository<PagoReclamacion, Long, Long> {

}
