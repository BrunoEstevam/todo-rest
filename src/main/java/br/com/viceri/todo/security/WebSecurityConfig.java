package br.com.viceri.todo.security;

import static br.com.viceri.todo.model.Constants.ACCESS_EXPIRE_AT;
import static br.com.viceri.todo.model.Constants.SECRET;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.viceri.todo.model.Constants;

public class WebSecurityConfig extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public WebSecurityConfig(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO MUDAR
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getParameter("email"), request.getParameter("password"));

		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();

		String accessToken = JWT.create().withExpiresAt(ACCESS_EXPIRE_AT).withSubject(user.getUsername())
				.withClaim("ROLE",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));

		String refreshToken = JWT.create().withExpiresAt(Constants.REFRESH_EXPIRE_AT).withSubject(user.getUsername())
				.withClaim("ROLE",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));

		response.addHeader("refreshToken", refreshToken);
		response.addHeader("accessToken", accessToken);
	}
}
