package com.melilla.gestPlanes.model.enums;

public enum Peticion {
	
	NINGUNO("-----"),
	COMPRA("adquirir mediante compra"),
	HIPOTECA("hipotecar con cualquier entidad bancaria"),
	ADQUIRIR_50("adquirir el 50% de la finca registral nº"),
	ADQUIRIR_50_MULTIPLE("adquirir el 50% de las fincas registrales nºs"),
	DONACION("adquirir mediante donación de D.Dª"),
	APORTAR("aportar a la sociedad"),
	ADQUIRIR("adquirir la finca registral nº");
	
	private final String name;
	
	public static Peticion[] ALL = {NINGUNO,COMPRA,HIPOTECA,ADQUIRIR_50,ADQUIRIR_50_MULTIPLE,DONACION,APORTAR,ADQUIRIR};
	
	private Peticion(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}
	

}
