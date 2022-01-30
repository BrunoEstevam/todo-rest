package br.com.viceri.todo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.dto.TaskSaveRequest;
import br.com.viceri.todo.dto.TaskUpdateRequest;
import br.com.viceri.todo.model.Status;
import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.service.TaskService;
import br.com.viceri.todo.util.TaskMock;

@ExtendWith(SpringExtension.class)
class TaskControllerTest {
	
	@InjectMocks
	private TaskController taskController;
	
	@Mock
	private TaskService taskService;
	
	@Mock
	private Principal mockPrincipal;
	
	@Mock
	private ModelMapper modelMapper;

	@BeforeEach
	public void setUp() {
		BDDMockito.when(mockPrincipal.getName()).thenReturn("emailSecundario@email.com");
		
		BDDMockito.when(taskService.save(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(TaskMock.task());
		BDDMockito.when(taskService.maskAsCompleted(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(TaskMock.taskAsCompelete());
		BDDMockito.when(taskService.update(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(TaskMock.task());
		BDDMockito.when(taskService.findByPriority(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(TaskMock.taskList());
		
		BDDMockito.when(modelMapper.map(ArgumentMatchers.isA(TaskSaveRequest.class), ArgumentMatchers.any())).thenReturn(TaskMock.task());
	}
	
	@Test
	@DisplayName("Quando passado a tarefa deverá ser salva")
	void givenTaskSaveRequest_WhenSave_thenReturnSavedTask() {
		Task entity = taskController.save(TaskMock.taskRequestToBeSaved(), mockPrincipal);
	
		Assertions.assertThat(entity.getId()).isNotNull();
		Assertions.assertThatNoException();
	}
	
	@Test
	@DisplayName("Quando passado um id de tarefa deverá retornar uma tarefa concluída")
	void givenTaskId_MaskAsComplete_thenReturnCompleteTask() {
		Task entity = taskController.maskAsCompleted(1L, mockPrincipal);
		
		assertEquals(entity.getStatus(), Status.COMPLETED);
		Assertions.assertThatNoException();
	}

	@Test
	@DisplayName("Quando passado o dto deverá atualizar a tarefa e igualar as propriedades")
	void givenIdAndTask_WhenUpdate_thenReturnUpdateTask() {
		TaskUpdateRequest taskUpdated = TaskMock.taskUpdate();
		Task entity = taskController.update(1L, taskUpdated, mockPrincipal);
		
		assertEquals(taskUpdated.getPriority(), entity.getPriority());
		assertEquals(taskUpdated.getDescription(), entity.getDescription());
		Assertions.assertThatNoException();
	}
	
	@Test
	@DisplayName("Quando passado o id a tarefa terá que ser deletetada")
	void givenIdTask_WhenDelete_thenNoException() {
		taskController.delete(TaskMock.task().getId(), mockPrincipal);
		
		Assertions.assertThatNoException();
	}

	@Test
	@DisplayName("Quando passado o um filtro de tarefas deve retornar uma lista com objetos e http status de ok")
	void givenTaskFilter_WhenFilterByPriority_thenReturnListOfTasks() {
		ResponseEntity<List<Task>> tasksResponse = taskController.findByPriority(TaskMock.taskFilter(), mockPrincipal);
		
		assertEquals(tasksResponse.getStatusCode(), HttpStatus.OK);
		assertThat(tasksResponse.getBody()).isNotNull();
		assertFalse(tasksResponse.getBody().isEmpty());
	}
	
	@Test
	@DisplayName("Quando passado o um filtro de tarefas deve retornar uma lista vazia e http status de no content")
	void givenTaskFilter_WhenFilterByPriority_thenReturnEmptyListOfTasks() {
		BDDMockito.when(taskService.findByPriority(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());
		ResponseEntity<List<Task>> tasksResponse = taskController.findByPriority(TaskMock.taskFilter(), mockPrincipal);
		
		assertEquals(tasksResponse.getStatusCode(), HttpStatus.NO_CONTENT);
		assertTrue(tasksResponse.getBody().isEmpty());
	}
}
