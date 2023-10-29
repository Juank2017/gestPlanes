package com.melilla.gestPlanes.DTO;

import java.util.Collection;

import com.melilla.gestPlanes.model.Role;

public interface UserDTO {

	Long getId();

	String getUserName();

	Boolean getEnabled();

	Collection<Role> getRoles();

}
