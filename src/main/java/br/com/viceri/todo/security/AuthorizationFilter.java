package br.com.viceri.todo.security;

import static br.com.viceri.todo.model.Constants.SUFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.viceri.todo.exception.RefreshTokenException;
import br.com.viceri.todo.model.User;
import br.com.viceri.todo.service.UserService;

public class AuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().contains("/login")) {
			filterChain.doFilter(request, response);

		} else {
			String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (!StringUtils.isEmpty(auth) && auth.startsWith(SUFIX)) {
				try {
					DecodedJWT decodedJWT = JwtUtil.decodeJwt(auth);
					List<SimpleGrantedAuthority> roles = new ArrayList<>();
					roles.addAll(decodedJWT.getClaim("ROLES").asList(String.class).stream()
							.map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList()));

					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							decodedJWT.getSubject(), null, roles);

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);

				} catch (Exception e) {
					Map<String, String> mensagens = new HashMap<>();
					mensagens.put("error", e.getMessage());

					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setStatus(HttpStatus.FORBIDDEN.value());
					new ObjectMapper().writeValue(response.getOutputStream(), mensagens);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

	public String generateAcessTokenByRefreshToken(HttpServletRequest request) {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.isEmpty(auth) && auth.startsWith(SUFIX)) {
			try {
				DecodedJWT decodedJWT = JwtUtil.decodeJwt(auth);
				User user = userService.findByEmail(decodedJWT.getSubject());

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

}
