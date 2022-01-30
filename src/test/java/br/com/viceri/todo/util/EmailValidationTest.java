package br.com.viceri.todo.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.viceri.todo.exception.InvalidDataException;

@ExtendWith(SpringExtension.class)
class EmailValidationTest {
	
	@InjectMocks
	private EmailValidation emailValidation;

	@Test
	@DisplayName("Quando passado um email válido deve retornar true")
	void givenEmail_WhenValid_thenReturnTrue() {
		assertTrue(EmailValidation.isValid("user@email.com"));
	}
	
	@Test
	@DisplayName("Quando passado email inválido deve lançar uma exception")
	void givenEmail_WhenValid_thenThrowException() {
		assertThrows(InvalidDataException.class, () -> {
			EmailValidation.isValid("email.example.com");
		});
	}
}
