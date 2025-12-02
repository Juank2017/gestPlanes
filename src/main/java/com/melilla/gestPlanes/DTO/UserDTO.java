package com.melilla.gestPlanes.DTO;

import java.util.Collection;

import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.Role;

public interface UserDTO {

	Long getId();

	String getUserName();

	long getPlanDeTrabajo();
	
	boolean getEnabled();
	
	boolean isDeleted();

	Collection<Role> getRoles();
	
	Long getVersion();

}
