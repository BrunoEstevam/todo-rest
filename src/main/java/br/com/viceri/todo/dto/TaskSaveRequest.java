package br.com.viceri.todo.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.viceri.todo.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskSaveRequest implements Serializable {

	private static final long serialVersionUID = 4708340309441896979L;

	@Size(max = 200)
	@NotEmpty(message = "Informar a descrição da tarefa")
	private String description;

	private Date dueDate;

	private Priority priority;
}
