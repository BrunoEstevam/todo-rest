package br.com.viceri.todo.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
	private int status;

	private String uid;

	private String message;

	private String exceptionClass;

	private Date date;
}
