package com.melilla.gestPlanes.DTO;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.security.core.GrantedAuthority;

import com.melilla.gestPlanes.model.Plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponseDTO {

	private HttpStatus estado;
	private String userName;
	private long userId;
	private Plan idPlan;
	private List<GrantedAuthority> roles;
	private String token;
	private String refreshToken;
}
