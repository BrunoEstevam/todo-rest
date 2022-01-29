package br.com.viceri.todo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	public Task save(@RequestBody Task entity, Principal principal) {
		return taskService.save(entity, principal.getName());
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping("/{id}")
	public Task update(@PathVariable(required = true) Long id, @RequestBody Task entity, Principal principal) {
		entity.setId(id);
		
		return taskService.update(entity, principal.getName());
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(required = true) Long id, Principal principal) {
		taskService.delete(id, principal.getName());
	}
}
