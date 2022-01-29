package br.com.viceri.todo.service;

import static br.com.viceri.todo.model.util.Constants.SUFIX;

import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.viceri.todo.exception.InvalidDataException;
import br.com.viceri.todo.exception.RefreshTokenException;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.model.util.PasswordConstraintValidator;
import br.com.viceri.todo.repository.impl.UserRepositoryImplmentation;
import br.com.viceri.todo.security.JwtUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoryImplmentation repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private PasswordConstraintValidator passwordConstraintValidator;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = findByEmail(email);

		if (null == user) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	public String generateAcessTokenByRefreshToken(HttpServletRequest request) {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.isEmpty(auth) && auth.startsWith(SUFIX)) {
			try {
				DecodedJWT decodedJWT = JwtUtil.decodeJwt(auth);
				User user = findByEmail(decodedJWT.getSubject());

				return JwtUtil.generateAcessToken(request, user);

			} catch (Exception e) {
				throw new RefreshTokenException("Não foi possivel gerar o acess token");
			}
		} else {
			throw new RefreshTokenException("Refresh token não encontrado");
		}
	}

	public User save(User entity) {
		isValid(entity);
		passwordConstraintValidator.isValid(entity.getPassword());
		
		try {
			entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
			return repository.save(entity);
			
		} catch (Exception e) {
			throw new InvalidDataException("Erro ao gravar usuario");
		}
	}
	
	public void update(Long id, User entity) {
		isValid(entity);
		User user = findById(id);
		
		if (StringUtils.isNotEmpty(entity.getPassword())) {
			passwordConstraintValidator.isValid(entity.getPassword());
		} else {
			// Seta a senha para nao ter enviar request
			entity.setPassword(user.getPassword());
		}
		
		entity.setId(id);
		
		repository.save(entity);
	}

	private void isValid(User entity) {
		try {
			User user = findByEmail(entity.getEmail());
			if (null != user && null != entity.getId() && user.getId() != entity.getId()) {
				throw new EntityExistsException();
			}

		} catch (IncorrectResultSizeDataAccessException | EntityExistsException e) {
			throw new EntityExistsException("Já existe um usuário cadastrado com o mesmo e-mail");
		}
	}

	public User findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public User findById(Long id) {
		return repository.findById(id);
	}
}
