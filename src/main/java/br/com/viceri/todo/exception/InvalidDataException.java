package br.com.viceri.todo.exception;

public class InvalidDataException extends RuntimeException {

	private static final long serialVersionUID = -4311392831492943335L;

	public InvalidDataException(String errorMessage) {
		super(errorMessage);
	}
}
