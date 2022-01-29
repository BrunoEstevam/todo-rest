package br.com.viceri.todo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.viceri.todo.exception.InvalidDataException;
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
		
		entity.setUser(user);
		entity.setCreateDate(new Date());
		
		isValid(entity);
		
		return repository.save(entity);
	}
	
	public Task update(Task entity, String email) {
		sameUser(entity.getId(), email);
		
		isValid(entity);
		
		return repository.update(entity);
	}

	public void delete(Long id, String email) {
		sameUser(id, email);
		
		repository.delete(id);
	}
	
	public Task findById(Long id) {
		return repository.findById(id);
	}

	private boolean sameUser(Long id, String email) {
		User user = userService.findByEmail(email);
		Task task = findById(id);
		
		if (null == user || !(user.getId() == task.getUser().getId())) {
			throw new AccessDeniedException("Você não tem acesso a esta tarefa");
		}
		
		return true;
	}
	
	private boolean isValid(Task entity) {
		if (null != entity.getDueDate() && entity.getCreateDate().after(entity.getDueDate())) {
			throw new InvalidDataException("Data de vencimento não pode ser antes da data de criação");
		}
		
		return true;
	}
}
