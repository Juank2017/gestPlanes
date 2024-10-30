package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.DocumentoProcedimientoReclamacion;

public interface DocumentoProcedimientoReclamacionRepository extends JpaRepository<DocumentoProcedimientoReclamacion, Long>, RevisionRepository<DocumentoProcedimientoReclamacion,Long,Long> {

}
