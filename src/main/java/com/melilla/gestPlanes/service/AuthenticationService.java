package com.melilla.gestPlanes.service;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;

public interface AuthenticationService {
    
	JwtResponseDTO login (String userName,String password);
	
	public boolean checkToken(String token,String userName);
}
