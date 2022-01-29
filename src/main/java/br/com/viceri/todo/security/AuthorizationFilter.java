package br.com.viceri.todo.security;

import static br.com.viceri.todo.model.util.Constants.SUFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationFilter extends OncePerRequestFilter {

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
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							decodedJWT.getSubject(), null, new ArrayList<>());

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
}
