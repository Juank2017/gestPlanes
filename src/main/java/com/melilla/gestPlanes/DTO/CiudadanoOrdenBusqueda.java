package com.melilla.gestPlanes.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CiudadanoOrdenBusqueda {

	 Long idPlan;
	 int pageIndex;
	 int pageSize;
	private List<CiudadanoCriterioOrden> sorting= new ArrayList<>();
	
	private List<CiudadanoCriterioBusqueda> columnFilters= new ArrayList<>();
}
