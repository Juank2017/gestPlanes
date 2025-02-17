package com.melilla.gestPlanes.model.config;

import com.melilla.gestPlanes.model.Plan;

import lombok.Data;

@Data
public class ConfigImpresionContratos {
	
	private long IdConfigImpresionContratos;
	
	private Plan plan;
	
	private ConfigImpresion configuracion;

}
