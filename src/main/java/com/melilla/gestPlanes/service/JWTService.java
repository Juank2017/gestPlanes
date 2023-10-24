package com.melilla.gestPlanes.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
	
	public String generateTokenFromUsername(String username);
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
