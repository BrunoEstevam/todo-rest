package br.com.viceri.todo.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.viceri.todo.dto.TaskFilterRequest;
import br.com.viceri.todo.exception.InvalidDataException;
import br.com.viceri.todo.model.Status;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.impl.TaskRepositoryImplmentation;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepositoryImplmentation repository;

	@Autowired
	private UserService userService;
	
	public Task save(Task entity, String email) {
		User user = userService.findByEmail(email);
		
		// Preenche a tarefa
		entity.setUser(user);
		entity.setStatus(Status.OPENED);
		entity.setCreateDate(new Date());
		
		isValid(entity);
		
		return repository.save(entity);
	}
	
	public Task update(Task entity, Long id, String email) {
		// Valida se o usuário tem permissao para mudar a tarefa
		Task task = sameUser(id, email);

		// Atualiza somente os campos que podem ser alterados
		if (StringUtils.isNotEmpty(entity.getDescription()) && !task.getDescription().equalsIgnoreCase(entity.getDescription())) {
			task.setDescription(entity.getDescription());
		}
		
		if (null != task.getPriority() && !task.getPriority().equals(entity.getPriority())) {
			task.setPriority(entity.getPriority());
		}

		return repository.update(task);
	}

	public Task maskAsCompleted(Long id, String email) {
		Task entity = sameUser(id, email);
		
		// Somente tarefas abertas podem ser concluídas
		if (entity.getStatus().equals(Status.COMPLETED)) {
			throw new InvalidDataException("Tarefa já está marcada como concluída");
		}

		entity.setStatus(Status.COMPLETED);

		return repository.update(entity);
	}
	
	public void delete(Long id, String email) {
		sameUser(id, email);
		
		repository.delete(id);
	}
	
	public Task findById(Long id) {
		return repository.findById(id);
	}
	
	public List<Task> findByPriority(TaskFilterRequest taskFilterRequest, String name) {
		return repository.findByPriority(taskFilterRequest, name);
	}

	
	// Verifica se o usuario tem permiossão para alterar a tarefa
	private Task sameUser(Long id, String email) {
		User user = userService.findByEmail(email);
		Task task = findById(id);
		
		if (null == user || !(user.getId() == task.getUser().getId())) {
			throw new AccessDeniedException("Você não tem acesso a está tarefa");
		}
		
		task.setUser(user);
		
		return task;
	}
	
	private boolean isValid(Task entity) {
		// Verifica se a due date é antes da data de criação
		if (null != entity.getDueDate() && entity.getCreateDate().after(entity.getDueDate())) {
			throw new InvalidDataException("Data de vencimento não pode ser antes da data de criação");
		}
		
		return true;
	}
}
