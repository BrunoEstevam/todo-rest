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
public class UserUpdateRequest implements Serializable {

	private static final long serialVersionUID = 7753281327302979215L;

	private Long id;

	private String name;
	
	private String password;
}
