package com.melilla.gestPlanes.DTO;

import lombok.Data;

@Data
public class UserWithRolDTO {
	
	long id;
	
	String userName;
	
	String roles;
	
	boolean enabled;
	boolean deleted;
	
	
	

}
