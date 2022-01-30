package br.com.viceri.todo.service;

import static br.com.viceri.todo.util.Constants.SUFIX;

import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.viceri.todo.exception.GenericError;
import br.com.viceri.todo.exception.RefreshTokenException;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.impl.UserRepositoryImplmentation;
import br.com.viceri.todo.security.JwtUtil;
import br.com.viceri.todo.util.EmailValidation;
import br.com.viceri.todo.util.PasswordConstraintValidator;

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
				throw new RefreshTokenException("Não foi possivel gerar o acess token", e);
			}
		} else {
			throw new RefreshTokenException("Refresh token não encontrado");
		}
	}

	public User save(User entity) {
		emailIsValid(entity);
		passwordConstraintValidator.isValid(entity.getPassword());

		try {
			entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
			return repository.save(entity);

		} catch (Exception e) {
			throw new GenericError("Erro ao gravar usuario", e);
		}
	}

	public User update(User entity, String email) {
		User user = repository.findById(entity.getId());
		User userToken = findByEmail(email);
		
		if (null == user) {
			throw new EmptyResultDataAccessException("Nenhum usuário encontrado", 1);

		} else if (user.getId() != userToken.getId()) {
			throw new AccessDeniedException("Você não tem acesso a este recurso");

		} else if (StringUtils.isNotEmpty(entity.getPassword())) {
			passwordConstraintValidator.isValid(entity.getPassword());

		} else {
			// Seta a senha para nao ter que enviar na request
			entity.setPassword(user.getPassword());
		}

		return repository.save(entity);
	}

	private User emailIsValid(User entity) {
		try {
			User user = findByEmail(entity.getEmail());
			if (null != user && user.getId() != entity.getId()) {
				throw new EntityExistsException();
			}

			EmailValidation.isValid(entity.getEmail());
			
			return user;
		} catch (IncorrectResultSizeDataAccessException | EntityExistsException e) {
			throw new EntityExistsException("Já existe um usuário cadastrado com o mesmo e-mail", e);
		}
	}

	public User findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public User findById(Long id) {
		return repository.findById(id);
	}
}
