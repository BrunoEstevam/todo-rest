package br.com.viceri.todo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {
	
	private static final long serialVersionUID = -7553580572880618645L;
	
	@JsonInclude(Include.NON_NULL)
	private String refreshToken;
	
	@JsonInclude(Include.NON_NULL)
	private String accessToken;

}
