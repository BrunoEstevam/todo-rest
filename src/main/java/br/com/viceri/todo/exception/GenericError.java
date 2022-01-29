package br.com.viceri.todo.exception;

public class GenericError extends RuntimeException {

	private static final long serialVersionUID = -4311392831492943335L;

	public GenericError(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public GenericError(String errorMessage) {
		super(errorMessage);
	}
}
