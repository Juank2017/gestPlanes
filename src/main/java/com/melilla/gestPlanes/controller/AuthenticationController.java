package com.melilla.gestPlanes.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.JwtResponseDTO;
import com.melilla.gestPlanes.DTO.LoginDTO;
import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.RefreshToken;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.service.AuthenticationService;
import com.melilla.gestPlanes.service.JWTService;
import com.melilla.gestPlanes.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@Log
public class AuthenticationController {

	@Autowired
	RefreshTokenService refreshTokenService;

	private final AuthenticationService authenticationService;

	@Autowired
	JWTService jwtService;

	@PostMapping("/login")
	public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {

		return ResponseEntity.ok(authenticationService.login(loginDTO.getUserName(), loginDTO.getPassword()));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody Map<String,String> request) {
			log.info(request.toString());
			String ref = request.get("refreshToken");
			log.info("ref: "+ref);
			RefreshToken refresh = refreshTokenService.findByToken(ref).orElseThrow(()-> new TokenRefreshException(request.get("refreshToken"), "no se encuentra"));
			refreshTokenService.verifyExpiration(refresh);
			User user = refresh.getUser();
			String token = jwtService.generateToken(user);
		
			return ResponseEntity.ok(JwtResponseDTO.builder()
							.estado(HttpStatus.OK)
							.token(token)
							.refreshToken(request.get("refreshtoken"))
							.userName(user.getUsername())
							.roles(user.getAuthorities().stream().collect(Collectors.toList()))
							.build());
				
	}
	
	@PostMapping("/checkToken")
	public ResponseEntity<ApiResponse>compruebaToken(@RequestBody Map<String,String> request){
		
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(authenticationService.checkToken(request.get("token"), request.get("userName")));
		response.setMensaje("Listado de usuarios");
		return ResponseEntity.ok(response);
	}

}
