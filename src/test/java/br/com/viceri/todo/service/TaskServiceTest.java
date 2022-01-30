package br.com.viceri.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.exception.InvalidDataException;
import br.com.viceri.todo.model.Status;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.repository.impl.TaskRepositoryImplmentation;
import br.com.viceri.todo.util.TaskMock;
import br.com.viceri.todo.util.UserMock;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

	@InjectMocks
	private TaskService taskService;

	@Mock
	private UserService userService;

	@Mock
	private TaskRepositoryImplmentation repository;

	@Mock
	private Principal mockPrincipal;

	@BeforeEach
	public void setUp() {
		BDDMockito.when(userService.findByEmail(ArgumentMatchers.contains("second@email.com.br")))
				.thenReturn(UserMock.userSaved());
		BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(TaskMock.task());
		BDDMockito.when(taskService.findById(ArgumentMatchers.any())).thenReturn(TaskMock.task());

		BDDMockito.when(repository.update(ArgumentMatchers.any())).thenReturn(TaskMock.taskAsCompelete());
		BDDMockito.when(mockPrincipal.getName()).thenReturn("second@email.com.br");

		BDDMockito.when(repository.findByPriority(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());
	}

	@Test
	@DisplayName("Quando salvar deve retornar uma tarefa")
	void givenTask_WhenSave_thenReturnTaskSaved() {
		Task entity = TaskMock.taskToBeSaved();
		Task task = taskService.save(entity, mockPrincipal.getName());

		Assertions.assertThat(task).isNotNull().isNotSameAs(entity);
		Assertions.assertThat(task.getId()).isNotNull();
	}

	@Test
	@DisplayName("Quando atualizar deve retornar uma tarefa")
	void givenTaskAndIdAndEmail_WhenUpdate_thenReturnTaskUpdated() {
		Task task = TaskMock.task();

		Task entity = taskService.update(task, task.getId(), mockPrincipal.getName());

		Assertions.assertThat(entity).isNotSameAs(task);
	}

	@Test
	@DisplayName("Quando atualizar deve verificar o email e lançar a exception")
	void givenTaskAndIdAndEmail_WhenUpdate_thenThrowException() {
		Task task = TaskMock.task();
		Task taskUsuarioDiferente = TaskMock.taskAsCompelete();
		taskUsuarioDiferente.getUser().setId(1l);

		BDDMockito.when(taskService.findById(ArgumentMatchers.any())).thenReturn(taskUsuarioDiferente);

		assertThrows(AccessDeniedException.class, () -> {
			taskService.update(task, task.getId(), mockPrincipal.getName());
		});
	}

	@Test
	@DisplayName("Quando enviar o id da tarefa deve retornar uma tarefa concluída")
	void givenTaskId_WhenMaskAsComplete_thenReturnCompleteTask() {
		Task completeTask = taskService.maskAsCompleted(TaskMock.task().getId(), mockPrincipal.getName());

		assertEquals(completeTask.getStatus(), Status.COMPLETED);
	}

	@Test
	@DisplayName("Quando enviar o id da tarefa de uma tarefa já concluída deve retornar um exception")
	void givenTaskId_WhenMaskAsComplete_thenThrowException() {
		Task entity = TaskMock.taskAsCompelete();

		BDDMockito.when(taskService.findById(ArgumentMatchers.any())).thenReturn(entity);
		BDDMockito.when(userService.findByEmail(ArgumentMatchers.anyString())).thenReturn(UserMock.userSaved());

		assertThrows(InvalidDataException.class, () -> {
			taskService.maskAsCompleted(TaskMock.taskAsCompelete().getId(), mockPrincipal.getName());
		});
	}

	@Test
	@DisplayName("Quando passado o id a tarefa terá que ser deletetada")
	void givenIdTask_WhenDelete_thenNoException() {
		taskService.delete(TaskMock.task().getId(), mockPrincipal.getName());
		
		Assertions.assertThatNoException();
	}
	
	@Test
	@DisplayName("Quando passado o id deve retornar a tarefa")
	void givenIdTask_WhenFindById_thenReturnTask() {
		taskService.findById(TaskMock.task().getId());
		
		Assertions.assertThatNoException();
	}
	
	@Test
	@DisplayName("Quando passado o um filtro de tarefas deve retornar uma lista com objetos e http status de ok")
	void givenTaskFilter_WhenFilterByPriority_thenReturnListOfTasks() {
		List<Task> entities = taskService.findByPriority(TaskMock.taskFilter(), mockPrincipal.getName());
		
		Assertions.assertThat(entities).asList();
	}

}
