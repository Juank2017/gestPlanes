package com.melilla.gestPlanes.model.config;

import com.melilla.gestPlanes.model.Categoria;

import lombok.Data;

@Data
public class ConfigImpresionPorCategoria extends ConfigImpresion {

	private Categoria categoria;
	
	private PlantillaContratoConfig plantilla;
}
