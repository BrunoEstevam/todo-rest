package br.com.viceri.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.viceri.todo.model.Role;
import br.com.viceri.todo.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController extends CommomBaseController{

	@Autowired
	private RoleService roleService;
	
	@ResponseStatus(value =  HttpStatus.CREATED)
	@PostMapping
	public Role saveRole(@RequestBody Role entity) {
		return roleService.saveRole(entity);
	}
	
}
