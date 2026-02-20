package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.ParteConfirmacion;

public interface ParteConfirmacionRepository extends JpaRepository<ParteConfirmacion, Long> {
	
	List<ParteConfirmacion>findAllByParteBajaIdParteBajaAndDeletedFalse(long idParteBaja);

}
