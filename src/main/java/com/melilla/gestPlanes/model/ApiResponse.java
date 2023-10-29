package com.melilla.gestPlanes.model;

import java.util.ArrayList;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

	private HttpStatus estado;
	
	
	private List<Object> payload = new ArrayList<Object>();
	
	private String mensaje;
}
