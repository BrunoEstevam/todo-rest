package br.com.viceri.todo.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
		return buildResponseEntity(ex, HttpStatus.NO_CONTENT);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		HashMap<String, String> details = new HashMap<>();

		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			details.put(error.getField(), error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			details.put(error.getObjectName(), error.getDefaultMessage());
		}

		return new ResponseEntity<>(new ApiError(status.value(), UUID.randomUUID().toString(), details,
				ex.getClass().getName(), new Date()), status);
	}

	@ExceptionHandler({ EntityExistsException.class })
	protected ResponseEntity<Object> handleException(EntityExistsException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ InvalidDataException.class })
	protected ResponseEntity<Object> handleException(InvalidDataException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ AccessDeniedException.class })
	protected ResponseEntity<Object> handleException(AccessDeniedException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ GenericError.class })
	protected ResponseEntity<Object> handleException(GenericError ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ PasswordConstraintException.class })
	protected ResponseEntity<Object> handleException(PasswordConstraintException ex, WebRequest request) {
		return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> buildResponseEntity(Throwable t, HttpStatus status) {
		String uid = UUID.randomUUID().toString();
		log.error(uid, t);

		HashMap<String, String> messages = new HashMap<>();
		messages.put("Message", t.getMessage());

		return new ResponseEntity<>(new ApiError(status.value(), uid, messages, t.getClass().getName(), new Date()),
				status);
	}
}
