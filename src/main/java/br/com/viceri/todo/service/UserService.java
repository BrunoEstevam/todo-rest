package br.com.viceri.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viceri.todo.dto.RoleRequest;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.impl.UserRepositoryImplmentation;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoryImplmentation repository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = findByEmail(email);
		
		if (null == user) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		user.setRoles(roleService.findByIdUser(user.getId()));

		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		roles.addAll(user.getRoles().stream().map(t -> new SimpleGrantedAuthority(t.getCode()))
				.collect(Collectors.toList()));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
	}

	public User saveUser(User entity) {
		User user = findByEmail(entity.getEmail());

		if (null == user) {
			throw new EntityExistsException("E-mail já cadastrado no sistema");
		}

		entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
		return repository.saveUser(entity);
	}
	
	public User findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public void addRoleToUser(RoleRequest roleRequest) {
		User user = findByEmail(roleRequest.getUserEmail());

		for (String code : roleRequest.getCodes()) {
			user.getRoles().add(roleService.findByCode(code));
		}
	}

}
