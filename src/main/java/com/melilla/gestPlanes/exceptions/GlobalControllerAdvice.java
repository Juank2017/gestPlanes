package com.melilla.gestPlanes.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.melilla.gestPlanes.exceptions.exceptions.ExpedienteNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;

import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

	
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(status, ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
      Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
          "Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
//	@ExceptionHandler({  })
//	public ResponseEntity<ApiError> handleEntityCreateError(Exception e) {
//		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
//	}

	@ExceptionHandler({io.jsonwebtoken.security.SignatureException.class})
	public ResponseEntity<ApiError> handleForbiden(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler({BadCredentialsException.class  })
	public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);

	}
	
	@ExceptionHandler({ExpiredJwtException.class  })
	public ResponseEntity<ApiError> handleExpiredJwt(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);

	}
	@ExceptionHandler({ TokenRefreshException.class })
	public ResponseEntity<ApiError> handleTokenRefresh(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler({ ExpedienteNotFoundException.class })
	public ResponseEntity<ApiError> handleNoEncontrado(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
}
