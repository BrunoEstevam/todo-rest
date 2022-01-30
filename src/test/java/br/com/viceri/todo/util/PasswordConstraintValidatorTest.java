package br.com.viceri.todo.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.viceri.todo.exception.PasswordConstraintException;

@ExtendWith(SpringExtension.class)
class PasswordConstraintValidatorTest {

	@InjectMocks
	private PasswordConstraintValidator passwordConstraintValidator;

	@BeforeEach
	public void setUp() {
		ReflectionTestUtils.setField(passwordConstraintValidator, "upper", 1);
		ReflectionTestUtils.setField(passwordConstraintValidator, "digit", 1);
		ReflectionTestUtils.setField(passwordConstraintValidator, "lower", 1);
		ReflectionTestUtils.setField(passwordConstraintValidator, "special", 1);
	}

	@Test
	@DisplayName("Quando passado uma senha válida deve retornar true")
	void givenPassword_WhenValid_thenReturnValidPassword() {
		boolean valid = passwordConstraintValidator.isValid("Ta1!");

		assertTrue(valid);
	}

	@Test
	@DisplayName("Quando passado uma senhas inválida deve lançar uma exception")
	void givenPassword_WhenValid_thenThrowException() {
		assertThrows(PasswordConstraintException.class, () -> {
			passwordConstraintValidator.isValid("Tacd1");
		});
		assertThrows(PasswordConstraintException.class, () -> {
			passwordConstraintValidator.isValid("Tacd!");
		});
		assertThrows(PasswordConstraintException.class, () -> {
			passwordConstraintValidator.isValid("T123!");
		});
		assertThrows(PasswordConstraintException.class, () -> {
			passwordConstraintValidator.isValid("acd1");
		});
		assertThrows(PasswordConstraintException.class, () -> {
			passwordConstraintValidator.isValid("Ta1");
		});
	}

}
