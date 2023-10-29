package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.model.User;

public interface UserService {
	UserDetailsService userDetailsService();
	
	User createUser(User user);
	
	User updateUser(User user);
	
	List<UserDTO> getUsers();
}
