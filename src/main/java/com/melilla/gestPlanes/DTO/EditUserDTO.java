package com.melilla.gestPlanes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDTO {

	private String userName;
	private boolean enabled;
	private String roles;
	private Long id;
	private boolean deleted;
	private Long version;
}
