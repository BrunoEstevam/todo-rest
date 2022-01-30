package br.com.viceri.todo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {

	private static final long serialVersionUID = 5344976941968077709L;

	private Long id;

	private String name;
	
	private String email;
	
	private String password;
}
