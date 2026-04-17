package com.melilla.gestPlanes.util;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log
public class DNIValidator {
	
	
	
	public static boolean validate(String dni) {
		
		//if (dni == null) return false;
		
		log.info(dni);
		
		dni = dni.toUpperCase();
		
		String letra = "TRWAGMYFPDXBNJZSQVHLCKET";
		
		String expresion_regular_dni = "^[XYZxyz]?[0-9]{7,8}[A-Za-z]$";
		
		if (dni.matches(expresion_regular_dni)) {
			
			//Extrae la letra final
			char letraFinal = dni.charAt(dni.length()-1);
			
			String parteNumeroDNI = dni.substring(0, dni.length()-1);
			
			parteNumeroDNI = parteNumeroDNI.replace("X", "0");
			
			parteNumeroDNI = parteNumeroDNI.replace("Y", "1");
			
			parteNumeroDNI = parteNumeroDNI.replace("Z", "2");
			
			int numeroDNI = Integer.parseInt(parteNumeroDNI);
			
			int indiceLetraDNI = numeroDNI%23;
			
			return (letra.charAt(indiceLetraDNI)==letraFinal);
			
		}else return false;
		
		
		
	}

}
