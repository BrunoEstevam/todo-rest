package br.com.viceri.todo.repository.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.CommomBaseRepository;
import br.com.viceri.todo.repository.UserRepositoryInterface;

@Repository
public class UserRepositoryImplmentation extends CommomBaseRepository {

	@Autowired
	private UserRepositoryInterface repositoryInterface;

	// Queries Nativas
	public User findByEmail(String email) {
		Map<String, Object> userMap = getJdbcTemplate().queryForMap("SELECT * FROM tb_user usr WHERE UPPER(email) = ?",
				email);

		return User.builder().id((Long) userMap.get("id")).email((String) userMap.get("email")).name((String) userMap.get("name"))
				.password((String) userMap.get("password")).build();
	}

	public User saveUser(User entity) {
		return repositoryInterface.save(entity);
	}
}
