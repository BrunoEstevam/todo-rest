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
public class UserResponse  implements Serializable{

	private static final long serialVersionUID = 5720089803014284256L;

	private Long id;

	private String name;
	
	private String email;
}