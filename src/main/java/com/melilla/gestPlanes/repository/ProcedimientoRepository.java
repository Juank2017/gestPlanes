package com.melilla.gestPlanes.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.Procedimiento;

public interface ProcedimientoRepository  extends JpaRepository<Procedimiento, Long>, RevisionRepository<Procedimiento, Long, Long> {
	
	
}
