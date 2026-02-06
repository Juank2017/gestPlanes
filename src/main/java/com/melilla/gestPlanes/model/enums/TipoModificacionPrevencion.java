package com.melilla.gestPlanes.model.enums;

public enum TipoModificacionPrevencion {
	
	FORMACION("FORMACION"),
	EVALUACION("EVALUACION"),
	RECONOCIMIENTO("RECONOCIMIENTO"),
	NINGUNO("");
	
public static TipoModificacionPrevencion [] ALL = {FORMACION,EVALUACION,RECONOCIMIENTO};
	
	private final String name;
	
	private TipoModificacionPrevencion(String name) {
		this.name = name;
		
	}
    public static TipoModificacionPrevencion forName(final String name) {
//       
        switch (name.toUpperCase()) {
		case "FORMACION":
			 return FORMACION;
		case "EVALUACION":
			return EVALUACION;
		case "RECONOCIMIENTO":
			return RECONOCIMIENTO;
	
		default:
			return NINGUNO;
			
		}
    
        
    }
	public String getName() {
		return this.name;
	}

}
