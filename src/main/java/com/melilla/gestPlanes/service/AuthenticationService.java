package com.melilla.gestPlanes.service;

import org.springframework.http.ResponseEntity;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    
	JwtResponseDTO login (String userName,String password);
	public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response);
	public boolean checkToken(String token,String userName);
	
	
	public User obtenerUsuarioLogado();
}
