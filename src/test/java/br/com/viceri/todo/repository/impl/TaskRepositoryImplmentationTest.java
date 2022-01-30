package br.com.viceri.todo.repository.impl;

import java.security.Principal;
import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.model.Task;
import br.com.viceri.todo.repository.TaskRepositoryInterface;
import br.com.viceri.todo.util.TaskMock;

@ExtendWith(SpringExtension.class)
class TaskRepositoryImplmentationTest {
	
	@InjectMocks
	private TaskRepositoryImplmentation taskRepositoryImplmentation;

	@Mock
	private TaskRepositoryInterface taskRepository;
	
	@Mock
    private JdbcTemplate jdbcTemplate;
	
	@Mock
	private Principal mockPrincipal;
	
	@Mock
	private DataSource dataSource;
	
	@BeforeEach
	public void setUp() {
		BDDMockito.when(taskRepository.save(ArgumentMatchers.any())).thenReturn(TaskMock.task());
		BDDMockito.when(mockPrincipal.getName()).thenReturn("second@email.com.br");
	}
	
	@Test
	@DisplayName("Quando salvar deve retornar uma tarefa")
	void givenTask_WhenSave_thenReturnTaskSaved() {
		Task entity = taskRepositoryImplmentation.save(TaskMock.taskToBeSaved());
	
		Assertions.assertThat(entity).isNotNull().isNotSameAs(TaskMock.task());
		Assertions.assertThat(entity.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Quando atualizar deve retornar uma tarefa")
	void givenTaskAndIdAndEmail_WhenUpdate_thenReturnTaskUpdated() {
		Task entity = taskRepositoryImplmentation.update(TaskMock.taskToBeSaved());
	
		Assertions.assertThat(entity).isNotNull().isNotSameAs(TaskMock.taskToBeSaved());
		Assertions.assertThat(entity.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Quando passado o id a tarefa terá que ser deletetada")
	void givenIdTask_WhenDelete_thenNoException() {
		taskRepositoryImplmentation.delete(TaskMock.task().getId());
	
		Assertions.assertThatNoException();
	}
}
