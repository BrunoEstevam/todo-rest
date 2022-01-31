package br.com.viceri.todo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest implements Serializable {

	private static final long serialVersionUID = 7753281327302979215L;

	private Long id;
	
	@Size(max = 200)
	@NotBlank(message = "Nome do usuário não pode ser vazio")
	private String name;
	
	@Size(min = 4)
	@Size(max = 200)
	@NotBlank(message = "Senha do usuário não pode ser vazia")
	private String password;
}
