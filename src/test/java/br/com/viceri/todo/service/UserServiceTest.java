package br.com.viceri.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.Principal;

import javax.persistence.EntityExistsException;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.impl.UserRepositoryImplmentation;
import br.com.viceri.todo.util.PasswordConstraintValidator;
import br.com.viceri.todo.util.UserMock;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepositoryImplmentation repositoryImplmentation;

	@Mock
	private PasswordConstraintValidator passwordConstraintValidator;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private Principal mockPrincipal;

	@BeforeEach
	public void setUp() {
		BDDMockito
				.when(repositoryImplmentation
						.findByEmail(ArgumentMatchers.contains("existEmailInDataBase@email.com.br")))
				.thenReturn(UserMock.userSaved());

		BDDMockito.when(repositoryImplmentation.save(ArgumentMatchers.any())).thenReturn(UserMock.userSaved());
		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.anyString()))
				.thenReturn(UserMock.userSaved());

		BDDMockito.when(passwordConstraintValidator.isValid(ArgumentMatchers.anyString())).thenReturn(true);
		BDDMockito.when(bCryptPasswordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("password");

		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.contains("notFound@email.com.br")))
				.thenReturn(null);
		BDDMockito.when(repositoryImplmentation.findById(ArgumentMatchers.anyLong())).thenReturn(UserMock.userUpdate());
		
		BDDMockito.when(mockPrincipal.getName()).thenReturn("emailSecundario@email.com");
	}

	@Test
	@DisplayName("Deve passar um email e retornar user details")
	void givenUser_WhenLoadByUsername_thenReturnUserDetails() {
		User userWithId = UserMock.userSaved();
		UserDetails userDetails = userService.loadUserByUsername("existEmailInDataBase@email.com.br");

		Assertions.assertThat(userDetails).isNotNull();
		assertEquals(userWithId.getEmail(), userDetails.getUsername());
		assertEquals(userWithId.getPassword(), userDetails.getPassword());
	}

	@Test
	@DisplayName("Deve passar um email e retornar uma exception de não encontrado")
	void givenUser_WhenLoadByUsernameNotFound_thenThrowException() {
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername("notFound@email.com.br");
		});
	}

	@Test
	@DisplayName("Deve passar um usuário e retornar um usuário salvo")
	void givenUser_WhenSave_thenReturnSavedUser() {
		User userToBeSaved = UserMock.userToBeSaved();
		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.contains("userToBeSaved@email.com.br")))
				.thenReturn(null);

		User userSaved = userService.save(userToBeSaved);

		Assertions.assertThat(userSaved.getId()).isNotSameAs(userToBeSaved);
		Assertions.assertThat(userSaved.getId()).isNotNull();
		Assertions.assertThatNoException();
	}

	@Test
	@DisplayName("Deve passar um usuário que já tem o email cadastrado e lançar uma exceção")
	void givenUser_WhenSaveExistingEmail_thenThrowException() {
		User entity = UserMock.userSaved();
		entity.setId(3l);

		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.contains(entity.getEmail())))
				.thenReturn(entity);

		Assertions.assertThatExceptionOfType(EntityExistsException.class)
				.isThrownBy(() -> userService.save(UserMock.userToBeSaved()));
	}

	@Test
	@DisplayName("Quando passar um usuário e retornar um usuário atualizado")
	void givenIdUserAndUserAndEmail_WhenUpdate_thenReturnUpdated() {
		User userBeforeUpdate = UserMock.userUpdate();

		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.any())).thenReturn(userBeforeUpdate);
		BDDMockito.when(repositoryImplmentation.save(ArgumentMatchers.any())).thenReturn(userBeforeUpdate);

		User entity = userService.update(userBeforeUpdate, mockPrincipal.getName());

		assertEquals(entity.getEmail(), entity.getEmail());
		assertEquals(entity.getName(), entity.getName());
	}

	@Test
	@DisplayName("Quando passar um usuário com o email diferente do token deve ser lançado uma exception")
	void givenIdUserAndUserAndEmail_WhenUpdateWithEmailDifferentToken_thenThrowException() {
		BDDMockito.when(repositoryImplmentation.findByEmail(ArgumentMatchers.any())).thenReturn(UserMock.userSaved());

		Assertions.assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> userService.update(UserMock.userUpdate(), mockPrincipal.getName()));
	}

	@Test
	@DisplayName("Quando passar um email deve retornar um usuário")
	void givenUserEmail_WhenFindByEmail_thenThrowException() {
		User user = userService.findByEmail("existEmailInDataBase@email.com.br");
		Assertions.assertThat(user).isNotNull();
	}

	@Test
	@DisplayName("Quando passar um email que não existente retornar um objeto vazio")
	void givenUserEmail_WhenFindByEmail_thenReturnEmptyObject() {
		User user = userService.findByEmail("notFound@email.com.br");
		Assertions.assertThat(user).isNull();
	}

	@Test
	@DisplayName("Quando passar um id deve retornar um objeto")
	void givenUserId_WhenFindById_thenReturnUser() {
		User user = userService.findById(1l);
		Assertions.assertThat(user).isNotNull();
	}

	@Test
	@DisplayName("Quando passar um id deve retornar um objeto vazio")
	void givenUserId_WhenFindById_thenReturnEmptyUser() {
		BDDMockito.when(repositoryImplmentation.findById(ArgumentMatchers.anyLong())).thenReturn(User.builder().build());
		
		User user = userService.findById(1l);
		Assertions.assertThat(user).isNotNull();
	}

}
