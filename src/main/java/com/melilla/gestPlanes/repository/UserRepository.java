package com.melilla.gestPlanes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.model.User;


public interface UserRepository extends  JpaRepository<User, Long> ,RevisionRepository<User, Long, Long>{
	Optional<User> findByUserName(String userName);
	
	@Query(value = "SELECT u FROM User u")
	List<UserDTO>myFindAll();
	
	@Query(value = "SELECT u FROM User u WHERE u.id = ?1")
	Optional<UserDTO>myFindById(Long id);
}
