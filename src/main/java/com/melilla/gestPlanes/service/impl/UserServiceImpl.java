package com.melilla.gestPlanes.service.impl;




import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreateUserDTO;
import com.melilla.gestPlanes.DTO.EditUserDTO;
import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.exceptions.exceptions.DataStaleException;
import com.melilla.gestPlanes.exceptions.exceptions.UserNotFoundException;
import com.melilla.gestPlanes.model.Role;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.repository.RoleRepository;
import com.melilla.gestPlanes.repository.UserRepository;
import com.melilla.gestPlanes.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
@Log
@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;
 
	@Autowired 
	private RoleRepository roleRepository;
	
	
	
	
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
        	
            @Override
            public UserDetails loadUserByUsername(String username) {
               return userRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
           
            }
        };
    }


	@Override
	public User createUser(CreateUserDTO usuario) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = usuario.getPassword();
		User nuevoUsuario = new User();
		nuevoUsuario.setActive(usuario.isEnabled());
		nuevoUsuario.setUserName(usuario.getUserName());
		nuevoUsuario.setPassword(passwordEncoder.encode(password));
		
	
		List<Role> roles = new ArrayList<Role>();
		try {
			roles.add(roleRepository.findByRoleName(usuario.getRoles()).orElseThrow(()->new RoleNotFoundException()));
		} catch (RoleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		nuevoUsuario.setRoles(roles);
		return userRepository.save(nuevoUsuario);
	}


	@Override
	public List<UserDTO> getUsers() {
		List<User> lista = userRepository.findAll();
		for (User userDTO : lista) {
			log.info(userDTO.getId().toString()+" "+userDTO.isEnabled()+" "+userDTO.isDeleted());
		}
		return userRepository.myFindAll();
	}


	
	
	@Override
	public User updateUser(EditUserDTO user) {
		
		User usuario = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("usuario no encontrado"));
		if (user.getVersion() != usuario.getVersion()) throw new DataStaleException();
		usuario.setUserName(user.getUserName());
		usuario.setActive(user.isEnabled());
		usuario.setVersion(user.getVersion());
		
		List<Role> roles = new ArrayList<Role>();
		try {
			roles.add(roleRepository.findByRoleName(user.getRoles()).orElseThrow(()->new RoleNotFoundException()));
		} catch (RoleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		usuario.setRoles(roles);
		
		if (!userRepository.existsById(usuario.getId())) {
			throw new UserNotFoundException(usuario.getUsername());
		};
		return userRepository.save(usuario);
	}


	@Override
	public void deleteUser(Long idUsuario) {
		if (!userRepository.existsById(idUsuario)) {
			throw new UserNotFoundException("no se ha encontrado el usuario con id "+idUsuario);
		}else {
			Optional<User> usuario = userRepository.findById(idUsuario);
			userRepository.delete(usuario.get());
		};

		return;
		
	}


	@Override
	public UserDTO getUser(Long idUsuario) {

		
		return userRepository.myFindById(idUsuario).orElseThrow(()->new UserNotFoundException("no se ha encontrado el usuario con id "+idUsuario));
	}


	@Override
	public boolean existeUsuario(String userName) {
		

		return userRepository.findByUserName(userName).isPresent();
	}
}
