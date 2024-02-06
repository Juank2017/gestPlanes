package com.melilla.gestPlanes.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.lang.Math;

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
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.DTO.VacantesResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.DestinoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.model.OrganismoOcupacion;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.CiudadanoSpecification;
import com.melilla.gestPlanes.repository.CiudadanoSpecificationBuilder;
import com.melilla.gestPlanes.repository.ContratoRepository;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoOcupacionRepository;
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
	
	@Autowired
	private OrganismoOcupacionRepository organismoOcupacionRepository;

	@Override
	public List<Ciudadano> getCiudadanos(Long idPlan) {

		return null;
	}

	@Override
	public Ciudadano getCiudadano(Long idCiudadano) {

		return ciudadanoRepository.findById(idCiudadano).orElseThrow(()->new CiudadanoNotFoundException(idCiudadano));
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
				.numeroOrdenSepe(trabajador.getNumeroOrdenSepe())
				.bajaLaboral(false)
				.bajaMaternal(false)
				.build());
		if(trabajador.getGc() != null) {
			Contrato nuevoContrato = contratoRepository
					.save(Contrato.builder().base(trabajador.getBase()).prorratas(trabajador.getProrratas())
							.residencia(trabajador.getResidencia()).total(trabajador.getTotal())
							.entidad((trabajador.getEntidad() !=null)? 
									organismoRepository.findById(trabajador.getEntidad())
									.orElseThrow(()->new OrganismoNotFoundException(trabajador.getEntidad())):null)
									
							.destino((trabajador.getDestino() != null)? destinoRepository.findById(trabajador.getDestino())
									.orElseThrow(() -> new DestinoNotFoundException(trabajador.getDestino())):null)
							.categoria((trabajador.getCategoria()!= null)?categoriaRepository.findById(trabajador.getCategoria())
									.orElseThrow(() -> new CategoriaNotFoundException(trabajador.getCategoria())):null)
							.ocupacion((trabajador.getOcu()!=null)?ocupacionRepository.findById(trabajador.getOcu())
									.orElseThrow(()-> new OcupacionNotFoundException(trabajador.getOcu())):null)
							.diasVacaciones((int)Math.round((trabajador.getDuracion()/30)*2.5))
							.duracion(trabajador.getDuracion())
							.fechaInicio((trabajador.getFechaInicio() != null)?trabajador.getFechaInicio(): null)
							.fechaFinal((trabajador.getFechaFinal()!= null)?trabajador.getFechaFinal():null)
							.turno((trabajador.getTurno() != null)?trabajador.getTurno():"MAÑANA")
							.porcentajeHoras("63")
							.gc(trabajador.getGc().toString())
							.ciudadano(nuevoCiudadano).build());
		}

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

	@Override
	public Optional<Ciudadano> getTrabajadorPorDNI(String DNI) {
		
		return ciudadanoRepository.findByDNI(DNI);
	}

	@Override
	public Ciudadano editaTrabajador(UpdateTrabajadorDTO2 trabajador) {

		Ciudadano ciudadano = ciudadanoRepository.findById(trabajador.getIdCiudadano()).orElseThrow(()->new CiudadanoNotFoundException(trabajador.getIdCiudadano()));
		
		ciudadano.setNombre(trabajador.getNombre());
		ciudadano.setApellido1(trabajador.getApellido1());
		ciudadano.setApellido2(trabajador.getApellido2());
		ciudadano.setDNI(trabajador.getDNI());
		ciudadano.setEmail(trabajador.getEmail());
		ciudadano.setEstado(trabajador.getEstado());
		ciudadano.setFechaNacimiento(trabajador.getFechaNacimiento());
		ciudadano.setSeguridadSocial(trabajador.getSeguridadSocial());
		ciudadano.setTelefono(trabajador.getTelefono());
		ciudadano.setSexo(trabajador.getSexo());
		ciudadano.setNumeroOrdenSepe(trabajador.getNumeroOrdenSepe());
		ciudadano.setBajaLaboral(trabajador.isBajaLaboral());
		ciudadano.setBajaMaternal(trabajador.isBajaMaternal());
		
		if(trabajador.getGc() != null) {
			Contrato contrato = ciudadano.getContrato();
			if(contrato == null) { 
				contrato = Contrato
						.builder()
						.gc(trabajador.getGc().toString())
						.fechaInicio((trabajador.getFechaInicio() != null)?trabajador.getFechaInicio():null)
						.fechaFinal((trabajador.getFechaFinal()!=null)?trabajador.getFechaFinal():null)
						.fechaExtincion((trabajador.getFechaExtincion() != null)?trabajador.getFechaExtincion():null)
						.categoria((trabajador.getCategoria() != null)?categoriaRepository.findById(trabajador.getCategoria()).orElseThrow(()->new CategoriaNotFoundException(trabajador.getCategoria())):null)
						.ocupacion((trabajador.getOcu() != null)?ocupacionRepository.findById(trabajador.getOcu()).orElseThrow(()->new OcupacionNotFoundException(trabajador.getOcu())):null)
						.destino((trabajador.getDestino() != null)?destinoRepository.findById(trabajador.getDestino()).orElseThrow(()->new DestinoNotFoundException(trabajador.getDestino())):null)
						.entidad((trabajador.getEntidad() != null)?organismoRepository.findById(trabajador.getEntidad()).orElseThrow(()->new OrganismoNotFoundException(trabajador.getEntidad())):null)
						.porcentajeHoras("63")
						.diasVacaciones(0)
						.duracion(trabajador.getDuracion())
						.base(trabajador.getBase())
						.prorratas(trabajador.getProrratas())
						.residencia(trabajador.getResidencia())
						.total(trabajador.getTotal())
						.ciudadano(ciudadano)
						.build();
			//contrato.setCategoria(new Categoria());
			//contrato.setOcupacion(new Ocupacion());
			//contrato.setDestino(new Destino());
			//contrato.setEntidad(new Organismo());
			//contrato.setGc(trabajador.getGc().toString());
			
			
				
			contrato=contratoRepository.save(contrato);
			
			
			
			}else {
				if(trabajador.getGc().toString() != contrato.getGc()) {
					contrato.setGc(trabajador.getGc().toString());
				}
				if(trabajador.getFechaInicio() != contrato.getFechaInicio()) {
					contrato.setFechaInicio(trabajador.getFechaInicio());
				}
				if(trabajador.getFechaFinal() != contrato.getFechaFinal()) {
					contrato.setFechaFinal(trabajador.getFechaFinal());
				}
				if (trabajador.getFechaExtincion() != contrato.getFechaExtincion()) {
					contrato.setFechaExtincion(trabajador.getFechaExtincion());
				}
				if (trabajador.getDuracion() != contrato.getDuracion()) {
					contrato.setDuracion(trabajador.getDuracion());
					}
				if (trabajador.getTurno() != contrato.getTurno()) {
					contrato.setTurno(trabajador.getTurno());
					}
				if(contrato.getCategoria() != null) {
					
					if (trabajador.getCategoria() != contrato.getCategoria().getIdCategoria()) {
						Categoria categoria = categoriaRepository.findById(trabajador.getCategoria()).orElseThrow(()->new CategoriaNotFoundException(trabajador.getCategoria()));
						
						contrato.setCategoria(categoria);
					}
					
				}else {
					Categoria categoria = categoriaRepository.findById(trabajador.getCategoria()).orElseThrow(()->new CategoriaNotFoundException(trabajador.getCategoria()));
					
					contrato.setCategoria(categoria);
				}
				
				if(contrato.getOcupacion() != null) {
					if (trabajador.getOcu() != contrato.getOcupacion().getIdOcupacion()) {
						Ocupacion ocupacion = ocupacionRepository.findById(trabajador.getOcu()).orElseThrow(()->new OcupacionNotFoundException(trabajador.getOcu()));
						contrato.setOcupacion(ocupacion);	
						}
				}else {
					if(trabajador.getOcu() != null) {
						Ocupacion ocupacion = ocupacionRepository.findById(trabajador.getOcu()).orElseThrow(()->new OcupacionNotFoundException(trabajador.getOcu()));
						contrato.setOcupacion(ocupacion);
					}

				}
		
				if(contrato.getEntidad() != null) {
					if (trabajador.getEntidad() != contrato.getEntidad().getIdOrganismo()) {
						Organismo organismo = organismoRepository.findById(trabajador.getEntidad()).orElseThrow(()->new OrganismoNotFoundException(trabajador.getEntidad()));
						contrato.setEntidad(organismo);
					}
				}else {
					if(trabajador.getEntidad() != null) {
						Organismo organismo = organismoRepository.findById(trabajador.getEntidad()).orElseThrow(()->new OrganismoNotFoundException(trabajador.getEntidad()));
						contrato.setEntidad(organismo);
					}

				}
				
				if(contrato.getDestino() != null) {
					if (trabajador.getDestino() != contrato.getDestino().getIdDestino()) {
						Destino destino = destinoRepository.findById(trabajador.getDestino()).orElseThrow(()-> new DestinoNotFoundException(trabajador.getDestino()));
						contrato.setDestino(destino);
						}
				}else {
					
					if(trabajador.getDestino() != null) {
						Destino destino = destinoRepository.findById(trabajador.getDestino()).orElseThrow(()-> new DestinoNotFoundException(trabajador.getDestino()));
						contrato.setDestino(destino);
					}

				}
	
				
			}
			

			ciudadano.setContrato(contrato);
		}
		
		
		return ciudadanoRepository.saveAndFlush(ciudadano);
	}

	@Override
	public List<Ciudadano> modificarEstado(List<ModificaEstadoDTO> trabajadores) {
		
		List<Ciudadano> trabajadoresModificados = new ArrayList<Ciudadano>();
		
		for (ModificaEstadoDTO modificaEstadoDTO : trabajadores) {
			
			Ciudadano trabajador = ciudadanoRepository.findById(modificaEstadoDTO.getIdCiudadano()).orElseThrow(()->new CiudadanoNotFoundException(modificaEstadoDTO.getIdCiudadano()));
			
			if(modificaEstadoDTO.getEstado().contains("FINALIZADO/A")) {
				trabajador.setEstado(modificaEstadoDTO.getEstado());
				if(trabajador.getContrato() != null) {
					Contrato contrato = trabajador.getContrato();
					contrato.setFechaExtincion(modificaEstadoDTO.getFecha());
					
					if(contrato.getFechaInicio() != null) {
						LocalDate fechaInicio = contrato.getFechaInicio();
						
						
					}
					
					
					contratoRepository.save(contrato);
				}
			}else {
				trabajador.setEstado(modificaEstadoDTO.getEstado());
			}
			
			ciudadanoRepository.save(trabajador);
			trabajadoresModificados.add(trabajador);
		}
		
		return trabajadoresModificados;
	}

	@Override
	public int trabajadoresContratadosOrganismoOcupacion(Long idOrganismo, Long idOcupacion,List<String> estados) {

		List<Ciudadano>trabajadores = ciudadanoRepository.findByContratoEntidadIdOrganismoAndContratoOcupacionIdOcupacionAndEstadoIn(idOrganismo, idOcupacion,estados);
		return (trabajadores != null)?trabajadores.size():0;
	}

	@Override
	public int trabajadoresPrevistosOrganismoOcupacion(Long idOrganismo, Long idOcupacion) {
		Long orgOcu = organismoOcupacionRepository.countByOrganismoIdOrganismoAndOcupacionIdOcupacion(idOrganismo, idOcupacion);
		return (Integer.parseInt(orgOcu.toString()));
	}

	@Override
	public VacantesResponseDTO vacantesOrganismoOcupacion(Long idOrganismo, Long idOcupacion) {
		List<String> estados = new ArrayList<String>();
		estados.add("CONTRATADO/A");
		estados.add("FINALIZADO/A");
		int contratados = trabajadoresContratadosOrganismoOcupacion(idOrganismo, idOcupacion,estados);
		estados = new ArrayList<String>();
		estados.add("DESPEDIDO/A");
		estados.add("RENUNCIA");
		int parciales = trabajadoresContratadosOrganismoOcupacion(idOrganismo, idOcupacion,estados);
		List<OrganismoOcupacion> orgOcu = organismoOcupacionRepository.findByOrganismoIdOrganismoAndOcupacionIdOcupacion(idOrganismo, idOcupacion);
		int previstos = 0;
		for (OrganismoOcupacion organismoOcupacion : orgOcu) {
			previstos = previstos + organismoOcupacion.getNTrabajadores();
		}
		
		Ocupacion ocu = ocupacionRepository.findById(idOcupacion).orElseThrow(()-> new OcupacionNotFoundException(idOcupacion));
		Organismo org = organismoRepository.findById(idOrganismo).orElseThrow(()->new OrganismoNotFoundException(idOrganismo));
		
		VacantesResponseDTO vacantes = new VacantesResponseDTO();
		
		vacantes.setOrganismo(org.getNombreCortoOrganismo());
		vacantes.setOcupacion(ocu.getOcupacion());
		vacantes.setContratados(contratados);
		vacantes.setParciales(parciales);
		vacantes.setPrevistos(previstos);
		vacantes.setVacantes(previstos-(contratados+parciales));
		return vacantes;
	}

}
