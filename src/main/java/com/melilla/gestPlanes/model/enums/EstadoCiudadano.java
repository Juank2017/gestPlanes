package com.melilla.gestPlanes.model.enums;

public enum EstadoCiudadano {
	NINGUNO(""),
	CANDIDATO("Candidato/a"),
	RECHAZADO("Rechaza"),
	DESCARTADO("Descartado/a"),
	CONTRATADO("Contratado/a"),
	FINALIZADO("Finalizado/a"),
	DESPEDIDO("Despedido/a"),
	BAJA("Baja"),
	RENUNCIA("Renuncia");
	
	public static EstadoCiudadano [] ALL = {NINGUNO,CANDIDATO,RECHAZADO,DESCARTADO,CONTRATADO,FINALIZADO,DESPEDIDO,BAJA,RENUNCIA};
	
	private final String name;
	
	private EstadoCiudadano(String name) {
		this.name = name;
		
	}
    public static EstadoCiudadano forName(final String name) {
//        if (name == null) {
//            throw new IllegalArgumentException("Name cannot be null for type");
//        }
        switch (name.toUpperCase()) {
		case "CANDIDATO/A":
			 return CANDIDATO;
		case "RECHAZA":
			return RECHAZADO;
		case "DESCARTADO/A":
			return DESCARTADO;
		case "CONTRATADO/A":
			return CONTRATADO;
		case "FINALIZADO/A":
			return FINALIZADO;
		case "DESPEDIDO":
			return DESPEDIDO;
		case "BAJA":
			return BAJA;
		case "RENUNCIA":
			return RENUNCIA;
		default:
			return NINGUNO;
			
		}
    
        
    }
	public String getName() {
		return this.name;
	}
}
