package br.com.viceri.todo.util;

import br.com.viceri.todo.dto.UserRequest;
import br.com.viceri.todo.dto.UserResponse;
import br.com.viceri.todo.dto.UserUpdateRequest;
import br.com.viceri.todo.model.User;

public class UserMock {

	public static User userToBeSaved() {
		return User.builder().email("userToBeSaved@email.com.br").name("Teste").password("Teste1!").build();
	}
	
	public static User userSaved() {
		return User.builder().id(2l).email("second@email.com.br").name("Teste").password("Teste1!").build();
	}
	
	public static User userUpdate() {
		return User.builder().id(1l).email("usuarioAtualizado@email.com").name("Nome atualizado").password("teste").build();
	}

	// DTOs
	public static UserRequest userWithInvalidPassword() {
		return UserRequest.builder().email("email@teste.com.br").name("Teste").password("teste").build();
	}
	
	public static UserUpdateRequest userUpdateRequest() {
		return UserUpdateRequest.builder().id(1l).name("Para Atualizar").password("teste").build();
	}
	
	public static UserRequest createUserResquest() {
		return UserRequest.builder().email("email@teste.com.br").name("Teste").password("Teste1!").build();
	}

	public static UserResponse userReponse() {
		return UserResponse.builder().id(1l).email("teste@email.com.br").name("Teste").build();
	}
}
