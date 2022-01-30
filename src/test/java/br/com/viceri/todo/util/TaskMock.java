package br.com.viceri.todo.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.viceri.todo.dto.TaskFilterRequest;
import br.com.viceri.todo.dto.TaskSaveRequest;
import br.com.viceri.todo.dto.TaskUpdateRequest;
import br.com.viceri.todo.model.Priority;
import br.com.viceri.todo.model.Status;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.model.User;

public class TaskMock {

	public static Task taskAsCompelete() {
		return Task.builder().description("Task As Complete").priority(Priority.ALTA).createDate(new Date()).status(Status.COMPLETED).user(User.builder().id(2l).build()).build();
	}
	
	public static Task task() {
		return Task.builder().id(1l).description("Task Teste").priority(Priority.ALTA).createDate(new Date()).status(Status.OPENED).user(User.builder().id(2l).build()).build();
	}
	
	public static TaskSaveRequest taskRequestToBeSaved() {
		return TaskSaveRequest.builder().description("Task Teste").priority(Priority.ALTA).build();
	}
	
	public static Task taskToBeSaved() {
		return Task.builder().description("Task Teste").priority(Priority.ALTA).build();
	}
	
	public static TaskUpdateRequest taskUpdate() {
		return TaskUpdateRequest.builder().description("Task Teste").priority(Priority.ALTA).build();
	}
	
	public static TaskFilterRequest taskFilter() {
		return TaskFilterRequest.builder().priority(Priority.ALTA.name()).build();
	}
	
	public static List<Task> taskList() {
		return Stream.of(task(), taskAsCompelete()).collect(Collectors.toList());
	}
}
