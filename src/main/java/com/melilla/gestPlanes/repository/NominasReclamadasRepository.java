package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.NominasReclamadas;

public interface NominasReclamadasRepository extends JpaRepository<NominasReclamadas, Long>,RevisionRepository<NominasReclamadas, Long, Long> {

}
