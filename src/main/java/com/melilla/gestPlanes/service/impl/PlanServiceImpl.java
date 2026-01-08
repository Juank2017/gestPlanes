package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melilla.gestPlanes.DTO.CreatePlanDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.DTO.importDTO.CategoriasImportDTO;
import com.melilla.gestPlanes.DTO.importDTO.DataImport;
import com.melilla.gestPlanes.DTO.importDTO.DatosPlanImportDTO;
import com.melilla.gestPlanes.DTO.importDTO.OcupacionesImportDTO;
import com.melilla.gestPlanes.DTO.importDTO.OrganismosImportDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.FileParseException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanConfigNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotEmptyException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.mappers.PlantillaContratoConfigMapperImpl;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.model.OrganismoOcupacion;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoOcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.repository.PlanConfigRepository;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.repository.PlantillaContratoRepository;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.AuthenticationService;
import com.melilla.gestPlanes.service.CategoriaService;
import com.melilla.gestPlanes.service.OcupacionService;
import com.melilla.gestPlanes.service.OrganismoService;
import com.melilla.gestPlanes.service.PlanConfigService;
import com.melilla.gestPlanes.service.PlanService;
import com.nimbusds.jose.util.IOUtils;

import java.nio.charset.Charset;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Service
@RequiredArgsConstructor
@Log
public class PlanServiceImpl implements PlanService {

	@Autowired
	PlanRepository planRepository;

	@Autowired
	PlanConfigService planConfigService;

	@Autowired
	PlantillaContratoRepository plantillaContratoRepository;

	@Autowired
	PlanConfigRepository planConfigRepository;

	@Autowired
	OrganismoRepository organismoRepository;

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	OcupacionRepository ocupacionRepository;

	@Autowired
	DestinoRepository destinoRepository;

	@Autowired
	CiudadanoRepository ciudadanoRepository;

	@Autowired
	SalarioRepository salarioRepository;

	@Autowired
	OrganismoOcupacionRepository organismoOcupacionRepository;

	@Override
	public List<Plan> getPlanes() {

		return planRepository.findAll();
	}

	@Override
	public Optional<Plan> getPlan(Long idPlan) {
		return planRepository.findById(idPlan);
	}

	@Override
	public Plan seleccionarPlan(Long idPlan) {
		List<Plan> planes = getPlanes();

		planes.forEach((plan) -> plan.setActivo(false));

		Plan planSeleccionado = planRepository.findById(idPlan)
				.orElseThrow(() -> new PlanNotFoundException("No se encuentra el plan con id: " + idPlan));

		planSeleccionado.setActivo(true);

		return planRepository.save(planSeleccionado);

	}

	public Plan crearPlan(CreatePlanDTO plan) {

		// List<Plan> planes = getPlanes();

		// planes.forEach((p)->p.setActivo(false));

		Plan nuevoPlan = new Plan();

		nuevoPlan.setActivo(plan.isActivo());
		nuevoPlan.setDenominacion(plan.getDenominacion());
		nuevoPlan = planRepository.save(nuevoPlan);
		// nuevoPlan.setConfig(planConfigService.crearConfig());

		return planRepository.save(nuevoPlan);
	}

	@Override
	@Transactional
	public Plan getPlanActivo() {

		/*
		 * log.info("Dentro de getPlanActivo"); Plan planActivo =
		 * planRepository.findByActivo(true) .orElseThrow(() -> new
		 * PlanNotFoundException("no hay plan activo")); log.info("obtenido el plan: " +
		 * planActivo.getDenominacion()); Authentication auth =
		 * SecurityContextHolder.getContext().getAuthentication(); User usuarioLogado =
		 * (User) auth.getPrincipal();
		 * 
		 * long planUsuarioLogado = usuarioLogado.getPlanDeTrabajo(); long idPlanActivo
		 * = planActivo.getIdPlan(); log.info("IdPlanTrabajo usuario logado: " +
		 * planUsuarioLogado); log.info("IdPlanActivo: " + idPlanActivo);
		 * 
		 * Plan result = (idPlanActivo == planUsuarioLogado)? planActivo:
		 * planRepository.findById(usuarioLogado.getPlanDeTrabajo()) .orElseThrow(() ->
		 * new PlanNotFoundException("Plan no encontrado")) ;
		 * 
		 * log.info("Plan activo: "+result.getIdPlan()+" " +result.getDenominacion());
		 */
		return planRepository.findByActivo(true)
				.orElseThrow(() -> new PlanNotFoundException("no hay plan activo"));

	}

	@Override
	public Plan asignarConfiguracion(PlanConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String copiarPlan(Long idPlan, String nombreNuevoPlan) {

		String result = "";

		Plan nuevoPlan = new Plan();
		nuevoPlan.setDenominacion(nombreNuevoPlan);
		nuevoPlan.setActivo(false);
		nuevoPlan = planRepository.save(nuevoPlan);

		// Obtener el plan antiguo
		Plan antiguoPlan = planRepository.findById(idPlan)
				.orElseThrow(() -> new PlanNotFoundException(idPlan.toString()));

		nuevoPlan = copiarPlantillas(nuevoPlan, antiguoPlan);

		result += copiarOrganismos(idPlan, nuevoPlan);

		result += copiaCategoriaOcupaciones(idPlan, nuevoPlan);

		return result;
	}

	private Plan copiarPlantillas(Plan nuevoPlan, Plan antiguoPlan) {
		List<PlantillaContratoConfig> plantillas = antiguoPlan.getPlantillas();

		List<PlantillaContratoConfig> plantillasPlanNuevo = new ArrayList<PlantillaContratoConfig>();

		Iterator<PlantillaContratoConfig> itPlantillas = plantillas.iterator();

		while (itPlantillas.hasNext()) {
			PlantillaContratoConfig p = itPlantillas.next();
			PlantillaContratoConfig nueva = new PlantillaContratoConfig();
			nueva.setActiva(p.isActiva());
			nueva.setActividadEconomica(p.getActividadEconomica());
			nueva.setCargoRepresentante(p.getCargoRepresentante());
			nueva.setCif(p.getCif());
			nueva.setClausulaAdicional(p.getClausulaAdicional());
			nueva.setClausulaEspecifica(p.getClausulaEspecifica());
			nueva.setCodigoCuentaCotizacion(p.getCodigoCuentaCotizacion());
			nueva.setCodigoMunicipioEmpresa(p.getCodigoMunicipioEmpresa());
			nueva.setCodigoPaisEmpresa(p.getCodigoPaisEmpresa());
			nueva.setCodigoPostalEmpresa(p.getCodigoPostalEmpresa());
			nueva.setDniRepresentante(p.getDniRepresentante());
			nueva.setDomicilioSocial(p.getDomicilioSocial());
			nueva.setHoras(p.getHoras());
			nueva.setMunicipioEmpresa(p.getMunicipioEmpresa());
			nueva.setNombre(p.getNombre());
			nueva.setNombreFicheroPlantilla(p.getNombreFicheroPlantilla());
			nueva.setNombreRepresentante(p.getNombreRepresentante());
			nueva.setPaisEmpresa(p.getPaisEmpresa());
			nueva.setPlan(nuevoPlan);
			nueva.setRazonSocial(p.getRazonSocial());
			nueva.setRegimen(p.getRegimen());
			nueva.setSepe(p.getSepe());
			nueva.setTextoConceptosSalariales(p.getTextoConceptosSalariales());
			nueva.setUrl(p.getUrl());
			plantillasPlanNuevo.add(plantillaContratoRepository.save(nueva));

		}

		nuevoPlan.setPlantillas(plantillasPlanNuevo);

		nuevoPlan = planRepository.save(nuevoPlan);
		return nuevoPlan;
	}

	private String copiaCategoriaOcupaciones(Long idPlan, Plan nuevoPlan) {

		String result = "";

		List<Categoria> categoriasPlanAntiguo = categoriaRepository.findAllByIdPlanIdPlan(idPlan);

		Iterator<Categoria> itCategorias = categoriasPlanAntiguo.iterator();

		// List<Ocupacion> ocupacionesPorCategoria = new ArrayList<Ocupacion>();

		List<Ocupacion> ocupacionesCategoria = new ArrayList<Ocupacion>();

		while (itCategorias.hasNext()) {

			Categoria cat = itCategorias.next();

			ocupacionesCategoria
					.addAll(ocupacionRepository.findAllByCategoriaIdCategoriaOrderByOcupacionAsc(cat.getIdCategoria()));

			Categoria nuevaCategoria = new Categoria();

			nuevaCategoria.setCategoria(cat.getCategoria());
			nuevaCategoria.setGrupo(cat.getGrupo());
			nuevaCategoria.setGrupoProfesionalPersonalLaboral(cat.getGrupoProfesionalPersonalLaboral());
			nuevaCategoria.setIdPlan(nuevoPlan);

			nuevaCategoria = categoriaRepository.save(nuevaCategoria);

			result += "Copiada categoria " + nuevaCategoria.getCategoria() + " \n";
			// mapaCategorias.put(cat.getIdCategoria(), nuevaCategoria.getIdCategoria());
			Iterator<Ocupacion> itOcupacion = ocupacionesCategoria.iterator();

			while (itOcupacion.hasNext()) {
				Ocupacion ocupacionPlanAntiguo = itOcupacion.next();

				Categoria categoria = nuevaCategoria;

				Ocupacion nuevaOcupacion = new Ocupacion();

				nuevaOcupacion.setCategoria(categoria);
				nuevaOcupacion.setOcupacion(ocupacionPlanAntiguo.getOcupacion());
				nuevaOcupacion.setOcupacionSEPE(ocupacionPlanAntiguo.getOcupacionSEPE());

				ocupacionRepository.save(nuevaOcupacion);
				result += "\tCopiada ocupacion " + nuevaOcupacion.getOcupacion() + " \n";
			}

		}
		return result;
	}

	private String copiarOrganismos(Long idPlan, Plan nuevoPlan) {

		String result = "";

		List<Organismo> organismosPlanAntiguo = organismoRepository
				.findAllByIdPlanIdPlanOrderByNombreCortoOrganismoAsc(idPlan);

		Iterator<Organismo> it = organismosPlanAntiguo.iterator();

		while (it.hasNext()) {
			Organismo org = it.next();

			Organismo nuevoOrg = new Organismo();

			nuevoOrg.setOrganismo(org.getOrganismo());
			nuevoOrg.setNombreCortoOrganismo(org.getNombreCortoOrganismo());
			nuevoOrg.setIdPlan(nuevoPlan);
			nuevoOrg = organismoRepository.save(nuevoOrg);
			result += "Copiado el organismo: " + nuevoOrg.getNombreCortoOrganismo() + " \n";
			copiarDestinosDeOrganismo(org, nuevoOrg);
		}

		return result;
	}

	private String copiarDestinosDeOrganismo(Organismo organismoAntiguo, Organismo OrganismoNuevo) {

		String result = "";

		List<Destino> destinos = destinoRepository
				.findAllByIdOrganismoIdOrganismoOrderByDestinoAsc(organismoAntiguo.getIdOrganismo());

		Iterator<Destino> iterator = destinos.iterator();

		while (iterator.hasNext()) {

			Destino destinoOld = iterator.next();

			Destino destinoNew = new Destino();

			destinoNew.setDestino(destinoOld.getDestino());
			destinoNew.setIdOrganismo(OrganismoNuevo);

			destinoNew = destinoRepository.save(destinoNew);

			result += "Creado el destino " + destinoNew.getDestino() + " del organismo " + OrganismoNuevo.getOrganismo()
					+ " \n";

		}
		return result;
	}

	@Override
	public Plan actualizarPlan(Long idPlan, String denominacion) {

		Plan plan = planRepository.findById(idPlan).orElseThrow(() -> new PlanNotFoundException("Plan no encontrado"));

		plan.setDenominacion(denominacion);

		return planRepository.save(plan);
	}

	@Override
	public String copiarPlanImportando(Long idPlan, String nombreNuevoPlan, MultipartFile file) {

		String result = "";

		// Creando el plan nuevo

		Plan nuevoPlan = new Plan();
		nuevoPlan.setDenominacion(nombreNuevoPlan);
		nuevoPlan.setActivo(false);

		nuevoPlan = planRepository.save(nuevoPlan);

		result = result + "Creado el plan " + nombreNuevoPlan + "\n";
		log.info(result);

		// Obtener el plan antiguo
		Plan antiguoPlan = planRepository.findById(idPlan)
				.orElseThrow(() -> new PlanNotFoundException(idPlan.toString()));

		// Se copian las plantillas

		nuevoPlan = copiarPlantillas(nuevoPlan, antiguoPlan);

		result = result + "\t copiadas plantillas.\n";
		log.info(result);

		Path fileStorageLocation = null;
		;

		try {

//			PlanConfig config = planConfigService.obtenerConfig(idPlan);
//			
//			String uploadDirPlanAntiguo = config.getUploadDir();
//			
//			String denominacionAntiguo=antiguoPlan.getDenominacion().replace("-","_");
//			String denominacionNuevo= nuevoPlan.getDenominacion().replace("-","_");
//			
//			String uploadDirNuevo = uploadDirPlanAntiguo.replace(denominacionAntiguo, denominacionNuevo);
//			
//			PlanConfig configNuevoPlan = nuevoPlan.getConfig();
//			
//			configNuevoPlan.setUploadDir(uploadDirNuevo);

			fileStorageLocation = Paths.get("datosImportacion").toAbsolutePath().normalize();

			Files.createDirectories(fileStorageLocation);
			// nombre del fichero
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			Path targetLocation = fileStorageLocation.resolve(fileName);

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		}

		catch (Exception e) {
			borrarPlan(nuevoPlan.getIdPlan());
			e.printStackTrace();
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		DataImport datos = procesaFile(file);

		HashMap<Long, Long> mapaCategorias = new HashMap<Long, Long>();
		HashMap<Long, Long> mapaOrganismos = new HashMap<Long, Long>();
		HashMap<Long, Long> mapaOcupaciones = new HashMap<Long, Long>();

		try {
			// crear los organismos
			if (datos.getOrganismos() != null) {

				List<OrganismosImportDTO> organismos = datos.getOrganismos();

				Iterator<OrganismosImportDTO> it = organismos.iterator();

				while (it.hasNext()) {

					OrganismosImportDTO org = it.next();
					Organismo nuevoOrganismo = new Organismo();
					nuevoOrganismo.setOrganismo(org.getOrganismo());
					nuevoOrganismo.setIdPlan(nuevoPlan);

					nuevoOrganismo = organismoRepository.save(nuevoOrganismo);

					result = result + "\t importando organismo con id " + org.getIdOrganismo() + " "
							+ org.getOrganismo() + " con nuevo id " + nuevoOrganismo.getIdOrganismo() + "\n";

					mapaOrganismos.put(org.getIdOrganismo(), nuevoOrganismo.getIdOrganismo());
				}
				log.info(result);
			} else
				throw new FileParseException(
						"Ha ocurrido un error al importar los organismos. Probablemente hay algún error en el archivo .json.");

			if (datos.getCategorias() != null) {

				List<CategoriasImportDTO> categorias = datos.getCategorias();

				Iterator<CategoriasImportDTO> it = categorias.iterator();

				while (it.hasNext()) {
					CategoriasImportDTO ct = it.next();

					Categoria nuevaCategoria = new Categoria();

					nuevaCategoria.setGrupoProfesionalPersonalLaboral(ct.getCategoria());
					nuevaCategoria.setGrupo(Integer.parseInt(ct.getSalario_grupo()));
					nuevaCategoria.setIdPlan(nuevoPlan);

					nuevaCategoria = categoriaRepository.save(nuevaCategoria);

					result = result + "\t importando categoria con id " + ct.getIdCategoria() + " " + ct.getCategoria()
							+ " con nuevo id " + nuevaCategoria.getIdCategoria() + "\n";

					mapaCategorias.put(ct.getIdCategoria(), nuevaCategoria.getIdCategoria());

				}
				log.info(result);

			} else
				throw new FileParseException(
						"Ha ocurrido un error al importar las categorias. Probablemente hay algún error en el archivo .json.");

			if (datos.getOcupaciones() != null) {

				List<OcupacionesImportDTO> ocupaciones = datos.getOcupaciones();
				Iterator<OcupacionesImportDTO> it = ocupaciones.iterator();

				while (it.hasNext()) {
					OcupacionesImportDTO oc = it.next();

					Ocupacion nuevaOcupacion = new Ocupacion();
					long id = oc.getCategoria_idcategoria();

					if (mapaCategorias.containsKey(id)) {
						long idCategoria = mapaCategorias.get(id);

						Categoria cat = categoriaRepository.findById(idCategoria)
								.orElseThrow(() -> new CategoriaNotFoundException(
										mapaCategorias.get(oc.getCategoria_idcategoria())));

						nuevaOcupacion.setCategoria(cat);
						nuevaOcupacion.setIdPlan(nuevoPlan);
						nuevaOcupacion.setOcupacionSEPE(oc.getCodigoSepe());
						nuevaOcupacion.setOcupacion(oc.getOcupacion());

						nuevaOcupacion = ocupacionRepository.save(nuevaOcupacion);

						result = result + "\t importando ocupación con id " + oc.getIdOcupacion() + " "
								+ oc.getOcupacion() + " con nuevo id " + nuevaOcupacion.getIdOcupacion() + "\n";
						// log.info(result);
						mapaOcupaciones.put(oc.getIdOcupacion(), nuevaOcupacion.getIdOcupacion());
					} else {
						result = result + "\t no se ha importado la ocupacion con id " + oc.getIdOcupacion() + " "
								+ oc.getOcupacion() + "\n ";

					}
					log.info(result);
				}

			} else
				throw new FileParseException(
						"Ha ocurrido un error al importar las ocupaciones. Probablemente hay algún error en el archivo .json.");

			if (datos.getDatosPlan() != null) {
				List<DatosPlanImportDTO> datosPlan = datos.getDatosPlan();

				Iterator<DatosPlanImportDTO> it = datosPlan.iterator();

				while (it.hasNext()) {

					DatosPlanImportDTO dato = it.next();

					OrganismoOcupacion nuevoDatoPlan = new OrganismoOcupacion();

					nuevoDatoPlan.setNTrabajadores(dato.getN_trabajadores());
					long idOcupacion = mapaOcupaciones.get(dato.getOcupacion_idocupacion());
					// log.info(dato.getOcupacion_idocupacion()+" \t");
					nuevoDatoPlan.setOcupacion(ocupacionRepository.findById(idOcupacion)
							.orElseThrow(() -> new OcupacionNotFoundException(idOcupacion)));
					long idOrganismo = mapaOrganismos.get(dato.getProyecto_organismo_idorganismo());
					// log.info(dato.getProyecto_organismo_idorganismo()+"\n");
					nuevoDatoPlan.setOrganismo(organismoRepository.findById(idOrganismo)
							.orElseThrow(() -> new OrganismoNotFoundException(idOrganismo)));

					nuevoDatoPlan = organismoOcupacionRepository.save(nuevoDatoPlan);

					result = result + "\t  " + dato.getProyecto_organismo_idorganismo() + " \t "
							+ dato.getOcupacion_idocupacion() + "\t " + dato.getN_trabajadores() + " a  "
							+ nuevoDatoPlan.getOrganismo().getIdOrganismo() + " \t"
							+ nuevoDatoPlan.getOcupacion().getIdOcupacion() + " \t " + nuevoDatoPlan.getNTrabajadores()
							+ "\n";

				}

				log.info(result);

				PlanConfig config = planConfigService.crearConfig();

				nuevoPlan.setConfig(config);
				config.setPlan(nuevoPlan);
				planRepository.save(nuevoPlan);

			}
		} catch (Exception e) {

			borrarPlan(nuevoPlan.getIdPlan());
			e.printStackTrace();
			throw new FileParseException("No se ha podido importar el plan." + e.getMessage() + "\n"
					+ e.getStackTrace().toString() + "\n" + e.getCause());
		}

		return result;
	}

	private DataImport procesaFile(MultipartFile file) {
		ObjectMapper objetMapper = new ObjectMapper();

		DataImport data = new DataImport();

		List<OrganismosImportDTO> organismos = new ArrayList<OrganismosImportDTO>();
		List<OcupacionesImportDTO> ocupaciones = new ArrayList<OcupacionesImportDTO>();
		List<CategoriasImportDTO> categorias = new ArrayList<CategoriasImportDTO>();
		List<DatosPlanImportDTO> datosPlan = new ArrayList<DatosPlanImportDTO>();
		try {

			JsonNode json = objetMapper
					.readTree(IOUtils.readInputStreamToString(file.getInputStream(), Charset.defaultCharset()));

			TypeReference<List<OrganismosImportDTO>> typeReferenceList = new TypeReference<List<OrganismosImportDTO>>() {
			};
			TypeReference<List<OcupacionesImportDTO>> typeReferenceListOcupaciones = new TypeReference<List<OcupacionesImportDTO>>() {
			};
			TypeReference<List<CategoriasImportDTO>> typeReferenceListCategorias = new TypeReference<List<CategoriasImportDTO>>() {
			};
			TypeReference<List<DatosPlanImportDTO>> typeReferenceListDatosPlan = new TypeReference<List<DatosPlanImportDTO>>() {
			};

			JsonNode JsonOrganismos = json.get("organismos");
			JsonNode JsonOcupaciones = json.get("ocupaciones");
			JsonNode JsonCategorias = json.get("categorias");
			JsonNode JsonDatosPlan = json.get("datosPlan");

			organismos = objetMapper.convertValue(JsonOrganismos, typeReferenceList);

			ocupaciones = objetMapper.convertValue(JsonOcupaciones, typeReferenceListOcupaciones);

			categorias = objetMapper.convertValue(JsonCategorias, typeReferenceListCategorias);

			datosPlan = objetMapper.convertValue(JsonDatosPlan, typeReferenceListDatosPlan);

			data.setCategorias(categorias);
			data.setDatosPlan(datosPlan);
			data.setOcupaciones(ocupaciones);
			data.setOrganismos(organismos);

		} catch (Exception e) {
			throw new FileParseException(e.getMessage());
		}
		return data;
	}

	@Override
	public void borrarPlan(Long idPlan) {

		Plan plan = getPlan(idPlan).orElseThrow(() -> new PlanNotFoundException("Plan no encontrado"));

		List<Ciudadano> ciudadanosDelPlan = ciudadanoRepository.findAllByIdPlan(plan);

		if (!ciudadanosDelPlan.isEmpty())
			throw new PlanNotEmptyException(idPlan);

		// Borramos organismos y destinos di los tienen.

		List<Organismo> organismos = organismoRepository.findAllByIdPlanIdPlanOrderByNombreCortoOrganismoAsc(idPlan);
		
		List<OrganismoOcupacion> organismoOcupacion= organismoOcupacionRepository.findAllAgrupados(organismos);
		
		if (!organismoOcupacion.isEmpty()) {
			organismoOcupacionRepository.deleteAll(organismoOcupacion);
		}
		
		if (!organismos.isEmpty()) {

			organismos.forEach((o) -> {
				//Borra los destinos
				List<Destino> destinos = destinoRepository
						.findAllByIdOrganismoIdOrganismoOrderByDestinoAsc(o.getIdOrganismo());

				if (!destinos.isEmpty()) {
					destinos.forEach((d) -> {
						destinoRepository.delete(d);
					});
				}
				
				
				
				organismoRepository.delete(o);

			});

		}

		// Borramos categorias y ocupaciones.

		List<Categoria> categorias = categoriaRepository.findAllByIdPlanIdPlan(idPlan);

		if (!categorias.isEmpty()) {

			categorias.forEach((c) -> {

				List<Ocupacion> ocupaciones = c.getOcupaciones();

				if (!ocupaciones.isEmpty()) {
					ocupaciones.forEach((o) -> {
						ocupacionRepository.delete(o);
					});
				}
				categoriaRepository.delete(c);

			});

		}

		List<Salario> salarios = plan.getSalario();

		if (salarios != null && !salarios.isEmpty())
			salarios.forEach((s) -> {
				salarioRepository.delete(s);
			});

		List<PlantillaContratoConfig> plantillas = plan.getPlantillas();

		if (!plantillas.isEmpty())
			plantillas.forEach((p) -> {
				plantillaContratoRepository.delete(p);
			});

		PlanConfig config = plan.getConfig();

		if (config != null) {
			config.setPlan(null);
			planConfigRepository.delete(config);
		}
		
		

		planRepository.delete(plan);

	}

	@Override
	public Plan getWorikingPlan() {
		 Plan planActivo = planRepository.findByActivo(true) .orElseThrow(() -> new
				  PlanNotFoundException("no hay plan activo")); 
		 log.info("obtenido el plan: " + planActivo.getDenominacion());
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		 User usuarioLogado = (User) auth.getPrincipal();
				  
		 long planUsuarioLogado = usuarioLogado.getPlanDeTrabajo(); 
		 long idPlanActivo = planActivo.getIdPlan(); 
		 log.info("IdPlanTrabajo usuario logado: " + planUsuarioLogado); 
		 log.info("IdPlanActivo: " + idPlanActivo);
				  
		 Plan result = (idPlanActivo == planUsuarioLogado)? planActivo:
				  planRepository.findById(usuarioLogado.getPlanDeTrabajo()) .orElseThrow(() ->
				  new PlanNotFoundException("Plan no encontrado")) ;
				  
				  log.info("Plan activo: "+result.getIdPlan()+" " +result.getDenominacion());
		return result;
	}

}
