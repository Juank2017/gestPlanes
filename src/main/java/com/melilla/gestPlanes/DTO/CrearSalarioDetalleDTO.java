package com.melilla.gestPlanes.DTO;

import lombok.Data;

/**
 * DTO para recibir los datos del salario para un grupo de cotización
 */
@Data
public class CrearSalarioDetalleDTO {

	private int grupo;

	private String base;

	private String prorrata;

	private String residencia;

	private String total;

	private long idSalario;

}
