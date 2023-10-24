package com.melilla.gestPlanes.model.enums;

public enum EstadoCivil {

	NINGUNO(""),SOLTERO("Soltero/a"),CASADO("Casado/a"),DIVORCIADO("Divorciado/a"),VIUDO("Viudo/a");
	
	public static EstadoCivil [] ALL = {SOLTERO,CASADO,DIVORCIADO,VIUDO};
	
	private final String name;
	
	private EstadoCivil(String name) {
		this.name = name;
		
	}
    public static EstadoCivil forName(final String name) {
//        if (name == null) {
//            throw new IllegalArgumentException("Name cannot be null for type");
//        }
        switch (name.toUpperCase()) {
		case "SOLTERO/A":
			 return SOLTERO;
		case "CASADO/A":
			return CASADO;
		case "DIVORCIADO/A":
			return DIVORCIADO;
		case "VIUDO/A":
			return VIUDO;

		default:
			return NINGUNO;
			
		}
    
        
    }
	public String getName() {
		return this.name;
	}
	
}
