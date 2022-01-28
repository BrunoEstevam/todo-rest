package br.com.viceri.todo.security;

import static br.com.viceri.todo.model.Constants.ACCESS_EXPIRE_AT;
import static br.com.viceri.todo.model.Constants.SECRET;
import static br.com.viceri.todo.model.Constants.SUFIX;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.viceri.todo.model.Constants;

public class JwtUtil {

	public static String generateAcessToken(HttpServletRequest request, User userDetails) {
		String accessToken = JWT.create().withExpiresAt(ACCESS_EXPIRE_AT).withSubject(userDetails.getUsername())
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));
		return accessToken;
	}

	public static String generateAcessToken(HttpServletRequest request, br.com.viceri.todo.model.User user) {
		return JWT.create().withExpiresAt(ACCESS_EXPIRE_AT).withSubject(user.getEmail())
				.withIssuer(request.getRequestURL().toString()).sign(Algorithm.HMAC256(SECRET));
	}

	public static DecodedJWT decodeJwt(String auth) {
		String token = auth.substring(SUFIX.length());
		Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET);

		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		return decodedJWT;
	}
}
