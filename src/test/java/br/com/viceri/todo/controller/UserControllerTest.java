package br.com.viceri.todo.controller;

import java.security.Principal;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.dto.UserRequest;
import br.com.viceri.todo.dto.UserResponse;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.service.UserService;
import br.com.viceri.todo.util.UserMock;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private Principal mockPrincipal;
	
	@BeforeEach
	public void setUp() {
		// Save Mock
		BDDMockito.when(userService.save(ArgumentMatchers.any())).thenReturn(UserMock.userSaved());
		BDDMockito.when(modelMapper.map(ArgumentMatchers.isA(UserRequest.class), ArgumentMatchers.any())).thenReturn(UserMock.userToBeSaved());
		BDDMockito.when(modelMapper.map(ArgumentMatchers.isA(User.class), ArgumentMatchers.any())).thenReturn(UserMock.userReponse());
		BDDMockito.when(modelMapper.map(ArgumentMatchers.isA(User.class), ArgumentMatchers.any())).thenReturn(UserMock.userReponse());
		
		// Update
		BDDMockito.when(mockPrincipal.getName()).thenReturn("emailSecundario@email.com");
	}
	
	@Test
	@DisplayName("Quando passado o usuário deverá ser salvo")
	void givenUserRequest_WhenSave_thenReturnSavedUser() {
		UserResponse userSaved = userController.save(UserMock.createUserResquest());
	
		Assertions.assertThat(userSaved.getId()).isNotNull();
		Assertions.assertThatNoException();
	}
	
	@Test
	@DisplayName("Quando passado o usuário deverá ser atualizado")
	void givenIdAndUser_WhenUpdate_thenReturnUpdatedUser() {
		userController.update(1L, UserMock.userUpdateRequest(), mockPrincipal);
		
		Assertions.assertThatNoException();
	}
}
