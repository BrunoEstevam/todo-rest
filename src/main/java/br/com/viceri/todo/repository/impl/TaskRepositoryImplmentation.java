package br.com.viceri.todo.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.dto.TaskFilterRequest;
import br.com.viceri.todo.exception.GenericError;
import br.com.viceri.todo.model.Priority;
import br.com.viceri.todo.model.Status;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.model.User;
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

	public void delete(Long id) {
		try {
			repositoryInterface.deleteById(id);
		} catch (Exception e) {
			throw new GenericError("Erro ao deletar tarefa", e);
		}
	}

	public Task findById(Long id) {
		Map<String, Object> taskMap = getJdbcTemplate().queryForMap(
				"SELECT task.id AS taskId, * FROM tb_task task INNER JOIN tb_user usr ON usr.id = task.id_user WHERE task.id = ?",
				id);

		return Task.builder().id((Long) taskMap.get("taskId")).description((String) taskMap.get("description"))
				.createDate((Date) taskMap.get("create_date")).dueDate((Date) taskMap.get("due_date"))
				.priority(Priority.valueOf((String) taskMap.get("priority")))
				.status(Status.valueOf((String) taskMap.get("status")))
				.user(User.builder().name((String) taskMap.get("name")).email((String) taskMap.get("email"))
						.id((Long) taskMap.get("id_user")).build())
				.build();
	}

	public List<Task> findByPriority(TaskFilterRequest taskFilterRequest, String email) {
		String query = "SELECT task.id AS taskId, * FROM tb_task task INNER JOIN tb_user usr ON task.id_user = usr.id WHERE usr.email = '"
				+ email + "' AND task.status = '" + Status.OPENED + "' ";

		if (StringUtils.isNotEmpty(taskFilterRequest.getPriority())) {
			query += "AND task.priority = '" + taskFilterRequest.getPriority() + "' ";
		}

		return getJdbcTemplate()
				.query(String.format(query),
						(rs, rowNum) -> Task.builder().id((Long) rs.getLong("taskId"))
								.description((String) rs.getString("description"))
								.createDate((Date) rs.getDate("create_date")).dueDate((Date) rs.getDate("due_date"))
								.priority(Priority.valueOf((String) rs.getString("priority")))
								.status(Status.valueOf((String) rs.getString("status")))
								.user(User.builder().name((String) rs.getString("name"))
										.email((String) rs.getString("email")).id((Long) rs.getLong("id_user")).build())
								.build());
	}
}
