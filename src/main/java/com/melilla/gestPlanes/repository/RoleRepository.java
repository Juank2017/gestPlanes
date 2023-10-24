package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.Role;



public interface RoleRepository extends  JpaRepository<Role, Long> ,RevisionRepository<Role, Long, Long> {

}
