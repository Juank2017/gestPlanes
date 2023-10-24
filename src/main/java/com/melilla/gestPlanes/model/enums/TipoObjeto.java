package com.melilla.gestPlanes.model.enums;

public enum TipoObjeto {
	
	NINGUNO("-----"),
	DONACIONES("DONACIONES"),
	FINCA_RUSTICA("FINCA RÚSTICA"),
	FINCA_URBANA("FINCA URBANA"),
	GARAJE("GARAJE"),
	GARAJE_TRASTERO("GARAJE-TRASTERO"),
	HIPOTECA("HIPOTECA"),
	LOCAL("LOCAL"),
	NAVE("NAVE"),
	OBRA("OBRA"),
	PERMUTA("PERMUTA"),
	PROMOCION("PROMOCIÓN"),
	SOLAR_ESTADO("SOLAR DEL ESTADO");
	
	private String name;
	
	public static TipoObjeto[] ALL = {NINGUNO,DONACIONES,FINCA_RUSTICA,FINCA_URBANA,GARAJE,HIPOTECA,LOCAL,NAVE,OBRA,PERMUTA,PROMOCION,SOLAR_ESTADO };
	
	private TipoObjeto(String name) {
		this.name=name;
		}
	
	public String getName() {
		return this.name;
	}
	
	}
