package com.melilla.gestPlanes.exceptions;

import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.ComponenteEquipoDuplicadoException;
import com.melilla.gestPlanes.exceptions.exceptions.ContratoReclamadoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.ConvertStringToDateException;
import com.melilla.gestPlanes.exceptions.exceptions.DataStaleException;
import com.melilla.gestPlanes.exceptions.exceptions.DestinoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.EquipoCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.EquipoNoEncontradoException;
import com.melilla.gestPlanes.exceptions.exceptions.ExpedienteNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.NominnasReclamadasNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.NotaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.ProcedimientoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.ProcedimientoSinPeriodosException;
import com.melilla.gestPlanes.exceptions.exceptions.SalarioNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;
import com.melilla.gestPlanes.exceptions.exceptions.TrabajadorNoEsJefeException;
import com.melilla.gestPlanes.exceptions.exceptions.TrabajadorYaContratadoException;
import com.melilla.gestPlanes.exceptions.exceptions.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(status, ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}


	@ExceptionHandler({ DataStaleException.class })
	public ResponseEntity<ApiError> handleDataStaleError(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.PRECONDITION_FAILED, e.getMessage());
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(apiError);
	}
	
	
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		return new ResponseEntity<Object>("Access denied message here", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ ConvertStringToDateException.class })
	public ResponseEntity<ApiError> handleEntityCreateError(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler({ io.jsonwebtoken.security.SignatureException.class })
	public ResponseEntity<ApiError> handleForbiden(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler({ProcedimientoSinPeriodosException.class, BadCredentialsException.class,EquipoCreationException.class,TrabajadorNoEsJefeException.class, NumberFormatException.class, TrabajadorYaContratadoException.class,ComponenteEquipoDuplicadoException.class })
	public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);

	}

	@ExceptionHandler({ ExpiredJwtException.class })
	public ResponseEntity<ApiError> handleExpiredJwt(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);

	}

	@ExceptionHandler({ TokenRefreshException.class })
	public ResponseEntity<ApiError> handleTokenRefresh(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler( {NominnasReclamadasNotFoundException.class, ProcedimientoNotFoundException.class, ContratoReclamadoNotFoundException.class, NotaNotFoundException.class, DocumentoNotFoundException.class, OcupacionNotFoundException.class,
			CategoriaNotFoundException.class, DestinoNotFoundException.class, OrganismoNotFoundException.class,
			ExpedienteNotFoundException.class, CiudadanoNotFoundException.class, MyFileNotFoundException.class,
			PlanNotFoundException.class, UserNotFoundException.class, RoleNotFoundException.class, EquipoNoEncontradoException.class, SalarioNotFoundException.class })
	public ResponseEntity<ApiError> handleNoEncontrado(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler({ FileStorageException.class, DocumentCreationException.class })
	public ResponseEntity<ApiError> handleStoragException(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

}
