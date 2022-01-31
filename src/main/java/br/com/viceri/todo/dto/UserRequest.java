package br.com.viceri.todo.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

	@Size(max = 200)
	@NotEmpty(message = "Nome do usuário deve ser informado")
	private String name;
	
	@Email
	@Size(max = 200)
	@NotEmpty(message = "Eamil do usuário deve ser informado")
	private String email;
	
	@Size(min = 4, max = 200)
	@Size(max = 200)
	@NotEmpty(message = "Senha do usuário deve ser informada")
	private String password;
}
