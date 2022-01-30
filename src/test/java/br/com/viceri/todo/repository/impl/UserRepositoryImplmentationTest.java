package br.com.viceri.todo.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.repository.UserRepositoryInterface;
import br.com.viceri.todo.util.UserMock;

@ExtendWith(SpringExtension.class)
class UserRepositoryImplmentationTest {

	@InjectMocks
	private UserRepositoryImplmentation userRepositoryImplmentation;
	
	@Mock
	private UserRepositoryInterface userRepository;
	
	@BeforeEach
	public void setUp() {
		BDDMockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(UserMock.userSaved());
		BDDMockito.when(userRepositoryImplmentation.getJdbcTemplate()).thenReturn(new JdbcTemplate());
	}
}
