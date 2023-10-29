package com.melilla.gestPlanes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateUserDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.service.UserService;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@Log
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/usuarios")
	ResponseEntity<ApiResponse>getUsuarios(){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(userService.getUsers());
		response.setMensaje("Listado de usuarios");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value= "/crearUsuario")
	ResponseEntity<ApiResponse>crearUsuario(@RequestBody CreateUserDTO usuario,BindingResult result){
		
		log.info(usuario.toString());
		log.info(result.toString());
		String password = usuario.getPassword();
		
		
		
		User nuevoUsuario = new User();
		nuevoUsuario.setEnabled(usuario.isEnabled());
		nuevoUsuario.setUserName(usuario.getUserName());
		nuevoUsuario.setPassword(passwordEncoder.encode(password));
		nuevoUsuario.setRoles(usuario.getRoles());
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(userService.createUser(nuevoUsuario));
		response.setMensaje("Listado de usuarios");
		
		return ResponseEntity.ok(response);
	}

	@PutMapping("/actualizaUsuario")
	ResponseEntity<ApiResponse>actualizaUsuario(@RequestBody User user){
		
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(userService.updateUser(user));
		response.setMensaje("Listado de usuarios");
		
		return ResponseEntity.ok(response);
	}

}
