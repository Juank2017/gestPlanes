package com.melilla.gestPlanes.service.impl;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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
               return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
           
            }
        };
    }
}
