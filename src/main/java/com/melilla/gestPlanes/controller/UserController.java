package com.melilla.gestPlanes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateUserDTO;
import com.melilla.gestPlanes.DTO.EditUserDTO;
import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.service.RoleService;
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
	private RoleService roleService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/usuarios")
	ResponseEntity<ApiResponse>getUsuarios(){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(userService.getUsers());
		response.getPayload().add(roleService.getAllRoles());
		response.setMensaje("Listado de usuarios");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value= "/crearUsuario")
	ResponseEntity<ApiResponse>crearUsuario(@RequestBody CreateUserDTO usuario,BindingResult result){
		
		log.info(usuario.toString());
		log.info(result.toString());
		String password = usuario.getPassword();
		User nuevoUsuario = new User();
		nuevoUsuario.setActive(usuario.isEnabled());
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
	ResponseEntity<ApiResponse>actualizaUsuario(@RequestBody EditUserDTO user){
		
		log.info(user.toString());
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(userService.updateUser(user));
		response.setMensaje("Auctualizado usuario con id: "+ user.getId());
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/borraUsuario/{idUsuario}")
	ResponseEntity<ApiResponse>borraUsuario(@PathVariable String idUsuario){
				
		Long id = Long.parseLong(idUsuario);
		
		userService.deleteUser(id);
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		
		response.setMensaje("Borrado el usuario "+idUsuario);
		
		return ResponseEntity.ok(response);
		
	}

}
