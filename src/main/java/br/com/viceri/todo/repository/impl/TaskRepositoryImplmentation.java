package br.com.viceri.todo.repository.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.exception.GenericError;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.CommomBaseRepository;
import br.com.viceri.todo.repository.TaskRepositoryInterface;

@Repository
public class TaskRepositoryImplmentation extends CommomBaseRepository {

	@Autowired
	private TaskRepositoryInterface repositoryInterface;

	public Task save(Task entity) {
		try {
			return repositoryInterface.save(entity);

		} catch (Exception e) {
			throw new GenericError("Erro ao gravar tarefa", e);
		}
	}

	public Task update(Task entity) {
		try {
			return repositoryInterface.save(entity);

		} catch (Exception e) {
			throw new GenericError("Erro ao gravar tarefa", e);
		}
	}
	
	public Task findById(Long id) {
		Map<String, Object> taskMap = getJdbcTemplate().queryForMap("SELECT task.id AS taskId, * FROM tb_task task INNER JOIN tb_user usr ON usr.id = task.id_user WHERE task.id = ?", id);

		return Task.builder().id((Long) taskMap.get("taskId")).description((String) taskMap.get("description"))
				.createDate((Date) taskMap.get("create_date")).dueDate((Date) taskMap.get("due_date"))
				.user(User.builder().name((String) taskMap.get("name")).email((String) taskMap.get("email")).id((Long) taskMap.get("id_user")).build())
				.build();
	}

	public void delete(Long id) {
		try {
			repositoryInterface.deleteById(id);
		} catch (Exception e) {
			throw new GenericError("Erro ao deletar tarefa", e);
		}
	}
}
