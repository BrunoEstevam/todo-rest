package br.com.viceri.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.dto.RoleRequest;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseStatus(value =  HttpStatus.CREATED)
	@PostMapping
	public void saveUser(@RequestBody User entity) {
		userService.saveUser(entity);
	}
	
	@ResponseStatus(value =  HttpStatus.OK)
	@GetMapping(value = "/email")
	public User findByEmail(@RequestParam(value = "email") String email) {
		return userService.findByEmail(email);
	}
	
	@ResponseStatus(value =  HttpStatus.OK)
	@PostMapping("/add/user")
	public void addRoleToUser(@RequestBody RoleRequest roleRequest) {
		userService.addRoleToUser(roleRequest);
	}
	
}
