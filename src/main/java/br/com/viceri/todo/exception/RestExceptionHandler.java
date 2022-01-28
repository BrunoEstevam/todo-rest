package br.com.viceri.todo.exception;

import java.util.Date;
import java.util.UUID;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ EmptyResultDataAccessException.class, UsernameNotFoundException.class })
	protected ResponseEntity<Object> handleException(EmptyResultDataAccessException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ RefreshTokenException.class })
	protected ResponseEntity<Object> handleException(RefreshTokenException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.FORBIDDEN);
	}
	
	private ResponseEntity<Object> buildResponseEntity(Throwable t, HttpStatus status) {
		String uid = UUID.randomUUID().toString();
		log.error(uid, t);

		return new ResponseEntity<>(
				new ApiError(status.value(), uid, t.getMessage(), t.getClass().getName(), new Date()), status);
	}

}
