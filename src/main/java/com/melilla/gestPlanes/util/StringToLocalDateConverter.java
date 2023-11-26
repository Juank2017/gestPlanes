package com.melilla.gestPlanes.util;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.melilla.gestPlanes.exceptions.exceptions.ConvertStringToDateException;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String fecha) {
		try {
			LocalDate localdate= LocalDate.parse(fecha);
			return localdate;
		} catch (Exception e) {
			throw new ConvertStringToDateException("Hay un error en el fotmato de fecha");
		}
	
	}

}
