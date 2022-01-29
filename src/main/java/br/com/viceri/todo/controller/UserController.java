package br.com.viceri.todo.controller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.dto.UserRequest;
import br.com.viceri.todo.dto.UserResponse;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	public UserResponse save(@RequestBody UserRequest userResquet) {
		// Save and convert to DTO 
		return modelMapper.map(userService.save(modelMapper.map(userResquet, User.class)), UserResponse.class);
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PutMapping(value = "/{id}")
	public void update(@PathVariable(required = true) Long id, @RequestBody User entity) {
		userService.update(id, entity);
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/email")
	public User findByEmail(@RequestParam(value = "email", required = true) String email) {
		return userService.findByEmail(email);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/refresh-token")
	public ResponseEntity<?> generateAcessTokenByRefreshToken(HttpServletRequest request) {
		return ResponseEntity.ok(userService.generateAcessTokenByRefreshToken(request));
	}
}
