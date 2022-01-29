package br.com.viceri.todo.exception;

public class PasswordConstraintException extends RuntimeException {

	private static final long serialVersionUID = -4311392831492943335L;

	
	public PasswordConstraintException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public PasswordConstraintException(String errorMessage) {
		super(errorMessage);
	}
}
