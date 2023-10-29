package com.melilla.gestPlanes.DTO;

import java.util.List;

import com.melilla.gestPlanes.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

	private String userName;
	private String password;
	private boolean enabled;
	private List<Role> roles;
}
