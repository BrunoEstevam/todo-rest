package br.com.viceri.todo.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest implements Serializable {

	private static final long serialVersionUID = 5344976941968077709L;

	private Long id;

	private String userEmail;
	
	private String name;
	
	private String description;
	
	private List<String> codes;

}
