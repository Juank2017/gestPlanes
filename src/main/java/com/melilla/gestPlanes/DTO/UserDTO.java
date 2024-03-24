package com.melilla.gestPlanes.DTO;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.melilla.gestPlanes.model.Role;

public interface UserDTO {

	Long getId();

	String getUserName();

	
	boolean getEnabled();
	
	boolean isDeleted();

	Collection<Role> getRoles();
	
	Long getVersion();

}
