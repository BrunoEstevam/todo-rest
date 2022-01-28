package br.com.viceri.todo.model;

import java.util.Date;

public class Constants {

	public static final String SECRET = "37281-impossivel-de-descobrir-isso";
	
	public static final String ROLE_USER = "ROLE_USER";
	
	public static final Date ACCESS_EXPIRE_AT = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
	
	public static final Date REFRESH_EXPIRE_AT = new Date(System.currentTimeMillis() + 80 * 80 * 1000);
	
	public static final String SUFIX = "Bearer ";
}
