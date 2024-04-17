package com.melilla.gestPlanes.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @RequiredArgsConstructor
public class ApiError {
	


	@NonNull
	private HttpStatus estado;
//	@NonNull
//	@JsonFormat(shape = Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss")
//	private LocalDateTime fecha = LocalDateTime.now();
	
	@NonNull
	private String mensaje;

}
