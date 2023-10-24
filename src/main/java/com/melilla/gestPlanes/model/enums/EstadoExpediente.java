package com.melilla.gestPlanes.model.enums;

public enum EstadoExpediente {
	
	TRAMITE("Trámite"),
	CONCEDIDO("Concedido"),
	REQUERIDO("Requerido"),
	ARCHIVADO("Archivado"),
	DENEGADO("Denegado");
	
	private final String name;
	
	public static EstadoExpediente[] ALL = {TRAMITE,CONCEDIDO,REQUERIDO,ARCHIVADO,DENEGADO};
	
	private EstadoExpediente(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}
	

}
