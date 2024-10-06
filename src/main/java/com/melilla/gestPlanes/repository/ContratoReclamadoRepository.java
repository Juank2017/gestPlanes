package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.ContratoReclamado;

public interface ContratoReclamadoRepository extends JpaRepository<ContratoReclamado, Long> ,RevisionRepository<ContratoReclamado, Long, Long> {

	ContratoReclamado findByidContratoReclamado(long idContratoReclamado);
}
