package br.com.viceri.todo.exception;

public class RefreshTokenException extends RuntimeException {

	private static final long serialVersionUID = -4311392831492943335L;

	
	public RefreshTokenException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public RefreshTokenException(String errorMessage) {
		super(errorMessage);
	}
}
