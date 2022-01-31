package br.com.viceri.todo.exception;

import java.util.Date;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
	private int status;

	private String uid;

	private HashMap<String, String> messages;
	
	private String exceptionClass;

	private Date date;
}
