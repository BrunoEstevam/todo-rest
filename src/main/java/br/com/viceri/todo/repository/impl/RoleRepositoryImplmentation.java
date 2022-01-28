package br.com.viceri.todo.repository.impl;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.model.Role;
import br.com.viceri.todo.repository.CommomBaseRepository;
import br.com.viceri.todo.repository.RoleRepositoryInterface;

@Repository
public class RoleRepositoryImplmentation extends CommomBaseRepository {

	@Autowired
	private RoleRepositoryInterface repositoryInterface;

	// Queries Nativas
	public Role findByCode(String code) {
		return DataAccessUtils.singleResult(
				getJdbcTemplate().query("SELECT * FROM tb_role role WHERE UPPER(code) = " + code.toUpperCase(),
						new SingleColumnRowMapper<>(Role.class)));
	}

	public List<Role> findByIdUser(Long idUser) {
		return getJdbcTemplate().query(
				"SELECT role.id, role.code FROM tb_role_user role_user INNER JOIN tb_role role ON role.id = role_user.id_role WHERE id_user = " + idUser,
				(rs, rowNum) -> Role.builder().id(rs.getLong("id")).code(rs.getString("code")).build());
	}

	public Role saveRole(Role entity) {
		if (null == findByCode(entity.getCode())) {
			return repositoryInterface.save(entity);
		}

		throw new EntityExistsException("Role já cadastrada com o mesmo código");
	}

}
