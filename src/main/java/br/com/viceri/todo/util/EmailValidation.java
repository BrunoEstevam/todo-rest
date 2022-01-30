package br.com.viceri.todo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.viceri.todo.exception.InvalidDataException;

public class EmailValidation {
	private static final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	public static boolean isValid(String email) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			return true;
		}
		
		throw new InvalidDataException("Email inválido");
	}
}