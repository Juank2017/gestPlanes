package com.melilla.gestPlanes.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.melilla.gestPlanes.model.NotaCiudadano;



public interface NotasRepository extends JpaRepository<NotaCiudadano, Long>{
	
	
	@Query("SELECT n FROM NotaCiudadano n WHERE n.ciudadano.idCiudadano = ?1 ORDER BY n.pinned DESC,n.fechaNota DESC")
	List<NotaCiudadano> findByCiudadanoIdCiudadano(Long idCiudadano   );

}
