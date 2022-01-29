package br.com.viceri.todo.dto;

import java.io.Serializable;

import br.com.viceri.todo.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest implements Serializable{

	private static final long serialVersionUID = 3230043999787098995L;

	private String description;
	
	private Priority priority;
	
}
