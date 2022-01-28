package br.com.viceri.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viceri.todo.model.Role;
import br.com.viceri.todo.repository.impl.RoleRepositoryImplmentation;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepositoryImplmentation repository;

	public Role saveRole(Role entity) {
		return repository.saveRole(entity);
	}
	
	public List<Role> findByIdUser(Long idUser) {
		return repository.findByIdUser(idUser);
	}

	public Role findByCode(String code) {
		return repository.findByCode(code);
	}
}
