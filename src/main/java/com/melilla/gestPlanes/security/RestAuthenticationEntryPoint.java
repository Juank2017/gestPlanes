package com.melilla.gestPlanes.security;

import java.io.IOException;

import java.io.PrintWriter;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.melilla.gestPlanes.exceptions.ApiError;

import org.springframework.http.HttpStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{
	private final ObjectMapper mapper = new ObjectMapper();
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, authException.getMessage());
		mapper.findAndRegisterModules();
		String strApiError = mapper.writeValueAsString(apiError);
		
		PrintWriter writer = response.getWriter();
		writer.println(strApiError);
	}

}
