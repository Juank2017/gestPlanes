package com.melilla.gestPlanes.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO para recibir los datos necesarios para hacer una consulta de
 * trabajadores.
 * 
 */
@Data
@RequiredArgsConstructor
public class CiudadanoOrdenBusqueda {

	Long idPlan;
	// nº de página
	int pageIndex;
	// nº de registros por página
	int pageSize;
	// lista de campos con su orden ascendente o desdencente
	private List<CiudadanoCriterioOrden> sorting = new ArrayList<>();
	// Lista de campos de filtro y su valor.
	private List<CiudadanoCriterioBusqueda> columnFilters = new ArrayList<>();
}
