package com.melilla.gestPlanes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para crear un usuario de la aplicación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

	private String userName;
	private String password;
	private boolean enabled;
	private String roles;
}
