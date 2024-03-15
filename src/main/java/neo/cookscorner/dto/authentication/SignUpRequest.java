package neo.cookscorner.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.cookscorner.validations.EmailValidation;
import neo.cookscorner.validations.PasswordValidation;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String name;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    private String verifyPassword;

    public SignUpRequest(String name, String email, String password, String verifyPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verifyPassword = verifyPassword;
    }
}