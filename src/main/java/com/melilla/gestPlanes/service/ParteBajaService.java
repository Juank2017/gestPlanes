package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearParteBajaDTO;
import com.melilla.gestPlanes.DTO.CrearParteConfirmacionDTO;
import com.melilla.gestPlanes.DTO.EditaContingenciaDTO;
import com.melilla.gestPlanes.DTO.EditaParteBajaDTO;
import com.melilla.gestPlanes.model.ParteBaja;
import com.melilla.gestPlanes.model.TipoContingencia;

public interface ParteBajaService {
	
	List<ParteBaja> obtenerPartesBajaTrabajador(long idTrabajador);
	
	List<ParteBaja> obtenerPartesBajaTrabajadorPorDNI(String DNI);
	
	ParteBaja altaParteBaja (CrearParteBajaDTO parte);
	
	ParteBaja editaParteBaja (EditaParteBajaDTO parte);
	
	ParteBaja insertaParteConfirmacion(CrearParteConfirmacionDTO parte);
	
	List<TipoContingencia> listaContingencias();
	
	TipoContingencia crearContingencia(String contingencia);
	
	TipoContingencia editarContingencia(EditaContingenciaDTO contingencia);
	
	void borraContingencia(long idTipoContingencia);

}
