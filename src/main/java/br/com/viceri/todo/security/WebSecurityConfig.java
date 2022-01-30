package br.com.viceri.todo.security;

import static br.com.viceri.todo.util.Constants.ACCESS_EXPIRE_AT;
import static br.com.viceri.todo.util.Constants.SECRET;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.viceri.todo.util.Constants;

public class WebSecurityConfig extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public WebSecurityConfig(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getHeader("email"), request.getHeader("password"));

		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();

		String accessToken = generateAcessToken(request, user);

		String refreshToken = JWT.create().withExpiresAt(Constants.REFRESH_EXPIRE_AT).withSubject(user.getUsername())
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));

		Map<String, String> tokens = new HashMap<>();
		tokens.put("refreshToken", refreshToken);
		tokens.put("accessToken", accessToken);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}

	private String generateAcessToken(HttpServletRequest request, User user) {
		String accessToken = JWT.create().withExpiresAt(ACCESS_EXPIRE_AT).withSubject(user.getUsername())
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));
		return accessToken;
	}
}
