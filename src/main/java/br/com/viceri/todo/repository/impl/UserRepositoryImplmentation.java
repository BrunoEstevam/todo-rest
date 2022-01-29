package br.com.viceri.todo.repository.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.exception.GenericError;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.CommomBaseRepository;
import br.com.viceri.todo.repository.UserRepositoryInterface;

@Repository
public class UserRepositoryImplmentation extends CommomBaseRepository {

	@Autowired
	private UserRepositoryInterface repositoryInterface;

	public User save(User entity) throws GenericError {
		try {
			return repositoryInterface.save(entity);
		} catch (Exception e) {
			throw new GenericError("Erro ao gravar usuario");
		}
	}

	// Queries Nativas
	public User findByEmail(String email) {
		try {
			Map<String, Object> userMap = getJdbcTemplate()
					.queryForMap("SELECT * FROM tb_user usr WHERE UPPER(email) = ?", email);

			return User.builder().id((Long) userMap.get("id")).email((String) userMap.get("email"))
					.name((String) userMap.get("name")).password((String) userMap.get("password")).build();

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public User findById(Long id) {
		Map<String, Object> userMap = getJdbcTemplate().queryForMap("SELECT * FROM tb_user usr WHERE id = ?", id);

		return User.builder().id((Long) userMap.get("id")).email((String) userMap.get("email"))
				.name((String) userMap.get("name")).password((String) userMap.get("password")).build();
	}
}
