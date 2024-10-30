package com.melilla.gestPlanes.DTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class TotalesNominaReclamada {
	
	BigDecimal totalDevengado = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	BigDecimal totalPercibido = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	BigDecimal totalReclamado = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	BigDecimal totalCalculada = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	

}
