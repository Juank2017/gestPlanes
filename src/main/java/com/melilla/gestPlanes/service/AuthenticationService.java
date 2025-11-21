package com.melilla.gestPlanes.service;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;
import com.melilla.gestPlanes.model.User;

public interface AuthenticationService {
    
	JwtResponseDTO login (String userName,String password);
	
	public boolean checkToken(String token,String userName);
	
	
	public User obtenerUsuarioLogado();
}
