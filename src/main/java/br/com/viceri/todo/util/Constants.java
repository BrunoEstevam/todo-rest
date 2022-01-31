package br.com.viceri.todo.util;

import java.util.Calendar;
import java.util.Date;

public class Constants {

	public static final String SECRET = "37281-este-5644-texto-2131-generico";

	public static final Date ACCESS_EXPIRE_AT = getExpireDate();

	public static final Date REFRESH_EXPIRE_AT = getExpireDate();

	public static final String SUFIX = "Bearer ";

	// Adicona 15 Min no tempo de expiração do token
	private static Date getExpireDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 15);

		return cal.getTime();
	}
}
