package br.com.viceri.todo.util;

import java.util.Arrays;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.viceri.todo.exception.PasswordConstraintException;

@Component
public class PasswordConstraintValidator {

	@Value("${password.policy.upper}")
	private Integer upper;

	@Value("${password.policy.digit}")
	private Integer digit;

	@Value("${password.policy.lower}")
	private Integer lower;

	@Value("${password.policy.special}")
	private Integer special;

	public boolean isValid(String password) {
		PasswordValidator validator = new PasswordValidator(
				Arrays.asList(new LengthRule(8, 30), new CharacterRule(EnglishCharacterData.UpperCase, upper),
						new CharacterRule(EnglishCharacterData.LowerCase, lower),
						new CharacterRule(EnglishCharacterData.Digit, digit),
						new CharacterRule(EnglishCharacterData.Special, special),
						new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
						new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
						new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false), new WhitespaceRule()));

		RuleResult result = validator.validate(new PasswordData(new String(password)));

		if (!result.isValid()) {
			throw new PasswordConstraintException(
					"Senha não corresponde aos requisitos mínimos de segurança. A senha deve conter letras maiúsculas, minúsculas, números e pelo menos "
							+ special + " caractere especial EX:!@#$%&*");
		}

		return result.isValid();
	}
}