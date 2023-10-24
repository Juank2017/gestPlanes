package com.melilla.gestPlanes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.User;


public interface UserRepository extends  JpaRepository<User, Long> ,RevisionRepository<User, Long, Long>{
	Optional<User> findByUsername(String userName);
}
