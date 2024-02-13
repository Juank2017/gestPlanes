package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.DocumentoPlan;

public interface DocumentoPlanRepository extends JpaRepository<DocumentoPlan, Long>, RevisionRepository<DocumentoPlan, Long, Long>,JpaSpecificationExecutor<DocumentoPlan>{

}
