package com.melilla.gestPlanes.service.impl;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.UserDTO;
import com.melilla.gestPlanes.exceptions.exceptions.UserNotFoundException;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.repository.UserRepository;
import com.melilla.gestPlanes.service.UserService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;
 
	
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
	public User createUser(User user) {
		User usuarioGrabado = userRepository.save(user);
	
	
		return userRepository.save(user);
	}


	@Override
	public List<UserDTO> getUsers() {
		
		return userRepository.myFindAll();
	}


	@Override
	public User updateUser(User user) {
		if (!userRepository.existsById(user.getId())) {
			throw new UserNotFoundException(user.getUsername());
		};
		return userRepository.save(user);
	}
}
