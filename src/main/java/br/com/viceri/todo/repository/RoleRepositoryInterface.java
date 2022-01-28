package br.com.viceri.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.viceri.todo.model.Role;

@Repository
public interface RoleRepositoryInterface extends JpaRepository<Role, Long> {

}
