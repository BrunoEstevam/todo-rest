package br.com.viceri.todo.controller;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.dto.TaskFilterRequest;
import br.com.viceri.todo.dto.TaskSaveRequest;
import br.com.viceri.todo.dto.TaskUpdateRequest;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.service.TaskService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TaskService taskService;

	@ApiResponses(value = { @ApiResponse(description = "Cria a nova tarefa", responseCode = "201"),
			@ApiResponse(description = "Caso algum dado sejá inválido", responseCode = "400") })
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	public Task save(@RequestBody @Validated TaskSaveRequest taskRequest, Principal principal) {
		return taskService.save(modelMapper.map(taskRequest, Task.class), principal.getName());
	}

	@ApiResponses(value = { @ApiResponse(description = "Marca a tarefa como concluída", responseCode = "200"),
			@ApiResponse(description = "Caso a tarefa já esteja como concluída", responseCode = "400"),
			@ApiResponse(description = "Caso o usuário não tenha permissão para alterar a tarefa", responseCode = "403") })
	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/mark-as-complete/{id}")
	public Task maskAsCompleted(@PathVariable(required = true) Long id, Principal principal) {
		return taskService.maskAsCompleted(id, principal.getName());
	}

	@ApiResponses(value = { @ApiResponse(description = "Atualiza a tarefa", responseCode = "200"),
			@ApiResponse(description = "Caso o usuário não tenha permissão para alterar a tarefa", responseCode = "403") })
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping("/{id}")
	public Task update(@PathVariable(required = true) Long id, @RequestBody TaskUpdateRequest taskRequest,
			Principal principal) {
		return taskService.update(modelMapper.map(taskRequest, Task.class), id, principal.getName());
	}

	@ApiResponses(value = { @ApiResponse(description = "Deleta a tarefa", responseCode = "200"),
			@ApiResponse(description = "Caso o usuário não tenha permissão para alterar a tarefa", responseCode = "403") })
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(required = true) Long id, Principal principal) {
		taskService.delete(id, principal.getName());
	}

	@ApiResponses(value = { @ApiResponse(description = "Consulta as tarefas do usuário", responseCode = "200"),
			@ApiResponse(description = "Caso não encontre nenhuma tarefa", responseCode = "204") })
	@GetMapping("/findAll")
	public ResponseEntity<List<Task>> findByPriority(@RequestBody TaskFilterRequest taskFilterRequest,
			Principal principal) {
		List<Task> entities = taskService.findByPriority(taskFilterRequest, principal.getName());

		if (null == entities || entities.isEmpty()) {
			return new ResponseEntity<List<Task>>(entities, HttpStatus.NO_CONTENT);
		}

		return ResponseEntity.ok(entities);
	}

}
