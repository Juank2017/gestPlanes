package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CiudadanoCriterioOrden;
import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.DestinoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.CiudadanoSpecification;
import com.melilla.gestPlanes.repository.CiudadanoSpecificationBuilder;
import com.melilla.gestPlanes.repository.ContratoRepository;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class CiudadanoServiceImpl implements CiudadanoService {

	@Autowired
	private CiudadanoRepository ciudadanoRepository;

	@Autowired
	private OrganismoRepository organismoRepository;

	@Autowired
	private DestinoRepository destinoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private PlanService planService;
	
	@Autowired
	private OcupacionRepository ocupacionRepository;

	@Override
	public List<Ciudadano> getCiudadanos(Long idPlan) {

		return null;
	}

	@Override
	public Optional<Ciudadano> getCiudadano(Long idCiudadano) {

		return ciudadanoRepository.findById(idCiudadano);
	}

	@Override
	public Ciudadano crearCiudadano(Ciudadano ciudadano) {

		return ciudadanoRepository.save(ciudadano);
	}

	@Override
	public Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador) {

		Ciudadano nuevoCiudadano = ciudadanoRepository.save(Ciudadano.builder()
				.nombre(trabajador.getNombre().toUpperCase()).apellido1(trabajador.getApellido1().toUpperCase())
				.apellido2(trabajador.getApellido2().toUpperCase()).DNI(trabajador.getDNI().toUpperCase())
				.email(trabajador.getEmail()).esJefeEquipo(false).telefono(trabajador.getTelefono())
				.sexo(trabajador.getSexo()).seguridadSocial(trabajador.getSeguridadSocial())
				.idPlan(planService.getPlanActivo()).fechaRegistro(trabajador.getFechaRegistro())
				.fechaNacimiento(trabajador.getFechaNacimiento()).estado(trabajador.getEstado())

				.build());
		Contrato nuevoContrato = contratoRepository
				.save(Contrato.builder().base(trabajador.getBase()).prorratas(trabajador.getProrratas())
						.residencia(trabajador.getResidencia()).total(trabajador.getTotal())
						.entidad(organismoRepository.findById(trabajador.getEntidad())
								.orElseThrow(() -> new OrganismoNotFoundException(trabajador.getEntidad())))
						.destino(destinoRepository.findById(trabajador.getDestino())
								.orElseThrow(() -> new DestinoNotFoundException(trabajador.getDestino())))
						.categoria(categoriaRepository.findById(trabajador.getCategoria())
								.orElseThrow(() -> new CategoriaNotFoundException(trabajador.getCategoria())))
						.ocupacion(ocupacionRepository.findById(trabajador.getOcu())
								.orElseThrow(()-> new OcupacionNotFoundException(trabajador.getOcu())))
						.diasVacaciones(trabajador.getDuracion() == 6 ? 15 : null).duracion(trabajador.getDuracion())
						.fechaInicio(trabajador.getFechaInicio()).fechaFinal(trabajador.getFechaFinal())
						.turno(trabajador.getTurno()).porcentajeHoras("63").gc(trabajador.getGc().toString())
						.ciudadano(nuevoCiudadano).build());
		// log.info(nuevoContrato.toString());

		return nuevoCiudadano;
	}

	@Override
	public boolean existeTrabajador(String DNI) {

		return ciudadanoRepository.existsByDNI(DNI);
	}

	@Override
	public Page<Ciudadano> getTrabajadores(CiudadanoOrdenBusqueda ordenBusqueda) {

		Pageable page = null;
		Specification<Ciudadano> busqueda = null;
		Sort sort = null;
		Order orden = null;
		
		if (ordenBusqueda.getSorting() != null) {
			
			List<CiudadanoCriterioOrden> criterioOrden = ordenBusqueda.getSorting();
			
			List<Order> criteriosDeOrden = new ArrayList<>();
			

			
			for (CiudadanoCriterioOrden criterio : criterioOrden) {
				
				
				if (criterio.isDesc()) {
					orden = Order.desc(criterio.getId());
				}else {
					orden = Order.asc(criterio.getId());
				}
				criteriosDeOrden.add(orden);
			}
			
			sort = Sort.by(criteriosDeOrden);
			

		
			
			page = PageRequest.of(ordenBusqueda.getPageIndex(), ordenBusqueda.getPageSize(), sort);
		} else {
			page = PageRequest.of(ordenBusqueda.getPageIndex(), ordenBusqueda.getPageSize());
		}
		
		if(ordenBusqueda.getColumnFilters() != null) {
			CiudadanoSpecificationBuilder criterios = new CiudadanoSpecificationBuilder(ordenBusqueda.getColumnFilters(),planService);
			
			 busqueda = criterios.build();
			 if(busqueda != null) log.info(busqueda.toString());
		}

		return ciudadanoRepository.findAll(busqueda,page);

	}

}
