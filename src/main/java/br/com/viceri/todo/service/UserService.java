package br.com.viceri.todo.service;

import static br.com.viceri.todo.model.Constants.SUFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.viceri.todo.exception.RefreshTokenException;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.repository.impl.UserRepositoryImplmentation;
import br.com.viceri.todo.security.JwtUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoryImplmentation repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = findByEmail(email);

		if (null == user) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new  ArrayList<>());
	}
	
	public String generateAcessTokenByRefreshToken(HttpServletRequest request) {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.isEmpty(auth) && auth.startsWith(SUFIX)) {
			try {
				DecodedJWT decodedJWT = JwtUtil.decodeJwt(auth);
				User user = findByEmail(decodedJWT.getSubject());

				List<SimpleGrantedAuthority> roles = new ArrayList<>();
				roles.addAll(decodedJWT.getClaim("ROLES").asList(String.class).stream()
						.map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList()));

				return JwtUtil.generateAcessToken(request, user);
				
			} catch (Exception e) {
				throw new RefreshTokenException("Não foi possivel gerar o acess token");
			}
		} else {
			throw new RefreshTokenException("Refresh token não encontrado");
		}
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
}
