package com.melilla.gestPlanes.service.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;
import com.melilla.gestPlanes.exceptions.exceptions.UserNotFoundException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.RefreshToken;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.repository.UserRepository;
import com.melilla.gestPlanes.service.AuthenticationService;
import com.melilla.gestPlanes.service.JWTService;
import com.melilla.gestPlanes.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationManager authenticacionManager;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	private JWTService jwtService;

	public JwtResponseDTO login(String userName, String password) {

		try {

			Authentication auth = authenticacionManager
					.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

			User user = (User) auth.getPrincipal();

			var jwt = jwtService.generateToken(user);

			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

			return JwtResponseDTO.builder().estado(HttpStatus.OK).userName(user.getUsername()).idPlan(user.getIdPlan()).userId(user.getId()).roles(user.getAuthorities().stream().collect(Collectors.toList())).token(jwt)
					.refreshToken(refreshToken.getToken()).build();
		} catch (AuthenticationException e) {

			return JwtResponseDTO.builder().estado(HttpStatus.UNAUTHORIZED).build();
		}

	}
	
	@Transactional // Ensures that the logout process, including any database changes, is handled within a single transaction
	public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response) {
	    
	    // Retrieves the current authentication information from the SecurityContext
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Checks if the user is authenticated (authentication is not null and authenticated)
	    if (authentication != null && authentication.isAuthenticated()) {
	        
	        // Logs out the user by clearing their authentication info from the SecurityContext
	        new SecurityContextLogoutHandler().logout(request, response, authentication);

	        // Gets the Authorization header from the request to retrieve the JWT token
	        String token = request.getHeader("Authorization");

	        // Checks if the token is in "Bearer <token>" format, then removes "Bearer " prefix to extract only the token value
	        if (token != null && token.startsWith("Bearer ")) {
	            token = token.substring(7); // Extracts the actual token by removing the "Bearer " prefix
	        } else {
	            // If no valid token is provided, throws a custom exception indicating the refresh token does not exist
	            throw new TokenRefreshException("No existe el token", token);
	        }

	        // Searches the database for the provided token in the refreshTokenRepository
	        RefreshToken refreshToken = refreshTokenService.findByToken(token)
	                // If no matching token is found, throws an exception for a non-existing refresh token
	                .orElseThrow(() -> new TokenRefreshException("No existe el token",""));

	        // Sets the status of the found refresh token to REVOKED, marking it as unusable
	        refreshToken.setRefreshTokenStatus(false);
	        
	        // Saves the updated refresh token back to the database to persist the status change
	        refreshTokenService.save(refreshToken);

	        ApiResponse resp = new ApiResponse();
	        
	        resp.setEstado(HttpStatus.OK);
	        resp.setMensaje("Logout correcto");
	        
	        // Returns a successful response indicating that the logout process was completed
	        return ResponseEntity.ok(resp);
	    } else {
	        // Throws an exception if the user was not authenticated in the first place
	        throw new UserNotFoundException( "usuario no logado");
	    }
	}

	public boolean checkToken(String token, String userName) {
		var user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no válido"));

		return jwtService.isTokenValid(token, user);
	}
	
	public User obtenerUsuarioLogado() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		return (User) auth.getPrincipal();
	}

}
