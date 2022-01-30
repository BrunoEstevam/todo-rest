package br.com.viceri.todo.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.dto.TokenResponse;
import br.com.viceri.todo.dto.UserRequest;
import br.com.viceri.todo.dto.UserResponse;
import br.com.viceri.todo.dto.UserUpdateRequest;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@ApiResponses(value = { 
			@ApiResponse(description = "Cria um novo usuário", responseCode = "201"),
			@ApiResponse(description = "Caso já exista um usuário com o mesmo e-mail", responseCode = "400"),
			@ApiResponse(description = "Caso a senha não cumpra a poliítica de segurança", responseCode = "400")})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	public UserResponse save(@RequestBody UserRequest userResquet) {
		// Save and convert to DTO
		return modelMapper.map(userService.save(modelMapper.map(userResquet, User.class)), UserResponse.class);
	}

	@ApiResponses(value = { 
			@ApiResponse(description = "Atualiza o novo usuário", responseCode = "200"),
			@ApiResponse(description = "Caso o usuário do token não seja o mesmo que o da request", responseCode = "403"),
			@ApiResponse(description = "Caso a consulta por id não retorne nenhum usuário", responseCode = "204"),
			@ApiResponse(description = "Caso já exista um usuário com o mesmo e-mail", responseCode = "400"),
			@ApiResponse(description = "Caso a senha não cumpra a poliítica de segurança", responseCode = "400")})
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "/{id}")
	public void update(@PathVariable(required = true) Long id, @RequestBody UserUpdateRequest userResquet, Principal principal) {
		userResquet.setId(id);
		
		userService.update(modelMapper.map(userResquet, User.class), principal.getName());
	}

	@ApiResponses(value =  {
			@ApiResponse(description = "Retorna o novo access token", responseCode = "200"),
			@ApiResponse(description = "Caso tenha algum problema ao gerar o token", responseCode = "403"),
			@ApiResponse(description = "Caso o refresh token seja inválido", responseCode = "403")
	})
	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/refresh-token")
	public TokenResponse generateAcessTokenByRefreshToken(HttpServletRequest request) {
		return TokenResponse.builder().accessToken(userService.generateAcessTokenByRefreshToken(request)).build();
	}
}
