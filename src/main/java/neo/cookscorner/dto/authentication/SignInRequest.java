package neo.cookscorner.dto.authentication;

import lombok.Builder;
import neo.cookscorner.validations.EmailValidation;
import neo.cookscorner.validations.PasswordValidation;

@Builder
public record SignInRequest (
        @EmailValidation
        String email,
        @PasswordValidation
        String password
    ){}