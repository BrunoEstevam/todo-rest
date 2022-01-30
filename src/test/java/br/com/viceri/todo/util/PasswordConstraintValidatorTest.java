package br.com.viceri.todo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PasswordConstraintValidatorTest {
	
	@InjectMocks
	private PasswordConstraintValidator constraintValidator;
	
	@BeforeEach
	public void setUp() {
		
	}
		
	@Test
	@DisplayName("Valida a regra de política de senha está valida")
	void givenPassword_WhenValid_ReturnTrue() {
//		constraintValidator.isValid("Te1!");
	}

}
