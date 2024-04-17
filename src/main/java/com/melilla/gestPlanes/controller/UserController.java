package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateUserDTO;
import com.melilla.gestPlanes.DTO.EditUserDTO;
import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.model.ApiResponse;
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
	
	@GetMapping("/usuario/{id}")
	ResponseEntity<ApiResponse>getUsuario(@PathVariable Long id){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		UserDTO user= userService.getUser(id);
		response.getPayload().add(user);
		
		response.setMensaje("Obtenido usuario "+id);
		return ResponseEntity.ok().eTag(user.getVersion().toString()).body(response);
	}
	
	@PostMapping(value= "/crearUsuario")
	ResponseEntity<ApiResponse>crearUsuario(@RequestBody CreateUserDTO usuario,BindingResult result){
		
	
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(userService.createUser(usuario));
		response.setMensaje("Usuario "+ usuario.getUserName() +" creado");
		
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
	
	@GetMapping("existeUsuario/{userName}")
	ResponseEntity<ApiResponse>existeUsuario(@PathVariable String userName){
			
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		boolean resultado =  userService.existeUsuario(userName);
		response.getPayload().add(resultado);
		String mensaje = (resultado)?"El usuario "+userName+" ya existe.":"El usuario no existe"; 
		response.setMensaje(mensaje);
		return ResponseEntity.ok(response);
	}

}
