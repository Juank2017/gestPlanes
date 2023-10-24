package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;
import com.melilla.gestPlanes.DTO.LoginDTO;
import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;
import com.melilla.gestPlanes.model.RefreshToken;
import com.melilla.gestPlanes.service.AuthenticationService;
import com.melilla.gestPlanes.service.JWTService;
import com.melilla.gestPlanes.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor	
public class AuthenticationController {

	  @Autowired
	  RefreshTokenService refreshTokenService;
	
	private final AuthenticationService authenticationService;
	
	@Autowired
	JWTService jwtService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO){
		
		return ResponseEntity.ok(authenticationService.login(loginDTO.getUserName(), loginDTO.getPassword()));
	}
	
	  @PostMapping("/refreshtoken")
	  public ResponseEntity<?> refreshtoken(@Valid @RequestBody String request) {
	    

	    return refreshTokenService.findByToken(request)
	        .map(refreshTokenService::verifyExpiration)
	        .map(RefreshToken::getUser)
	        .map(user -> {
	          String token = jwtService.generateTokenFromUsername(user.getUsername());
	          return ResponseEntity.ok(JwtResponseDTO.builder().estado(HttpStatus.OK).token(token).refreshToken(request).userName(user.getUsername()).build());
	        })
	        .orElseThrow(() -> new TokenRefreshException(request,
	            "Refresh token is not in database!"));
	  }

}
