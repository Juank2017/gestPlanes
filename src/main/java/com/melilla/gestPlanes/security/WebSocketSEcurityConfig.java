package com.melilla.gestPlanes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSEcurityConfig implements WebSocketMessageBrokerConfigurer{
	
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .nullDestMatcher().authenticated() 
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll() 
                
                .simpSubscribeDestMatchers("/user/**", "/topic/documentos/*").hasRole("USER") 
                
                .anyMessage().denyAll(); 

        return messages.build();
    }

}
