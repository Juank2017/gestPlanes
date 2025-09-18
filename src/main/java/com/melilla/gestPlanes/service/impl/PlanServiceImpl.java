package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreatePlanDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.mappers.PlantillaContratoConfigMapperImpl;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.repository.PlanConfigRepository;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.repository.PlantillaContratoRepository;
import com.melilla.gestPlanes.service.CategoriaService;
import com.melilla.gestPlanes.service.OcupacionService;
import com.melilla.gestPlanes.service.OrganismoService;
import com.melilla.gestPlanes.service.PlanConfigService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

	private final PlantillaContratoConfigMapperImpl plantillaContratoConfigMapperImpl;

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
		nuevoPlan.setConfig(planConfigService.crearConfig(nuevoPlan));

		return planRepository.save(nuevoPlan);
	}

	@Override
	public Plan getPlanActivo() {

		return planRepository.findByActivo(true).orElseThrow(() -> new PlanNotFoundException("no hay plan activo"));
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

		result += copiarOrganismos(idPlan, nuevoPlan);

		result += copiaCategoriaOcupaciones(idPlan, nuevoPlan);

		return result;
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

}
