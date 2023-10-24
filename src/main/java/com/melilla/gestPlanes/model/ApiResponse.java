package com.melilla.gestPlanes.model;

import java.util.ArrayList;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse {

	private HttpStatus estado;
	
	private List<Object> payload = new ArrayList<Object>();
	
	private String mensaje;
}
